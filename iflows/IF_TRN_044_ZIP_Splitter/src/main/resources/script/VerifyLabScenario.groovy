import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput
import groovy.util.XmlSlurper
import java.security.MessageDigest
import java.security.Signature
import com.sap.it.api.ITApiFactory
import com.sap.it.api.keystore.KeystoreService

def semantic(node) {
    [node.name(),node.namespaceURI(),node.attributes(),node.children().collect {semantic(it)},node.children().size()==0 ? node.text() : null]
}
Message processData(Message message) {
    def c=message.getProperty('TRN_Case')
    if(c==null) throw new IllegalStateException('LAB_ASSERTION: no scenario context')
    def raw=message.getBody()
    byte[] bytes=message.getBody(byte[].class)
    if(bytes==null) bytes=(raw==null ? '' : raw.toString()).getBytes(c.encoding ?: 'UTF-8')
    String text=new String(bytes,c.encoding ?: 'UTF-8')
    def check={ boolean ok, String description -> if(!ok) throw new IllegalStateException('LAB_ASSERTION: '+c.id+': '+description+'; output='+text.take(300)) }
    if(c.negative) check(false,'Expected rejection from the native processing step, but it accepted the input')
    if(c.containsKey('exact')) check(text==c.exact,'Exact payload mismatch')
    if(c.oneOf) check(c.oneOf.contains(text),'Output is outside the documented alternatives')
    if(c.normalizedLines!=null) check(text.replace('\r\n','\n').trim()==c.normalizedLines,'CSV line values differ')
    if(c.uuid) check(text.trim().size()>=16,'Missing generated mapped ID')
    if(c.greeting) check(text==(message.getProperty('GreetingPrefix')+', '+message.getProperty('StudentName')+'!'),'Configured greeting was not applied')
    def searchable=text
    def xml=null
    if(c.xmlRoot || c.xmlValues || c.xmlValuesUnordered || c.xmlLeafValues || c.xmlEquivalent || c.storedValues) {
        xml=new XmlSlurper(false,true).parseText(text)
        searchable=xml.text()
        if(c.xmlRoot) check(xml.name()==c.xmlRoot,'Wrong XML root')
        if(c.xmlEquivalent) check(semantic(xml)==semantic(new XmlSlurper(false,true).parseText(c.xmlEquivalent)),'XML structure or values changed')
        if(c.xmlLeafValues) check(xml.depthFirst().findAll {it.children().size()==0}.collect {it.text()}==c.xmlLeafValues,'XML leaf values/order mismatch')
        (c.xmlValues ?: [:]).each {k,vals -> check(xml.depthFirst().findAll {it.name()==k}.collect {it.text()}==vals,'XML values/order mismatch for '+k)}
        (c.xmlValuesUnordered ?: [:]).each {k,vals -> check(xml.depthFirst().findAll {it.name()==k}.collect {it.text()}.sort()==vals.sort(),'Unordered XML values mismatch for '+k)}
    }
    if(c.storedValues) {
        def decoded=searchable
        xml.depthFirst().findAll {it.children().size()==0}.each {n ->
            try {decoded+=' '+new String(n.text().decodeBase64(),'UTF-8')} catch(Exception ignored) {}
        }
        c.storedValues.each {check(decoded.contains(it),'Selected datastore payload missing '+it)}
    }
    (c.contains ?: []).each {check(searchable.contains(it),'Missing value '+it)}
    (c.excludes ?: []).each {check(!searchable.contains(it),'Unexpected value '+it)}
    (c.headers ?: [:]).each {k,v -> check(message.getHeaders().get(k)?.toString()==v,'Header mismatch '+k)}
    if(c.signatureHeader) {
        def signed=message.getHeaders().get(c.signatureHeader)
        check(signed!=null && signed.toString().length()>50,'Missing signature header')
        def ks=ITApiFactory.getService(KeystoreService.class,null)
        def cert=ks.getCertificate('trn_canvas_rsa')
        def verifier=Signature.getInstance('SHA256withRSA')
        verifier.initVerify(cert.getPublicKey());verifier.update(c.input.getBytes('UTF-8'))
        check(verifier.verify(signed.toString().decodeBase64()),'Independent RSA public-key verification failed')
    }
    def role=null
    if(c.cmsParts) {
        if(Arrays.equals(bytes,c.input.getBytes('UTF-8'))) role='content'
        else {check(bytes.length>100 && (bytes[0]&0xff)==0x30,'Expected binary CMS signature information');role='signature'}
    }
    def hash=MessageDigest.getInstance('SHA-256').digest(bytes).encodeBase64().toString()
    def result=[scenarioId:c.id,status:c.observe?'OBSERVED':'PASS',objective:c.objective,variant:c.variant,input:c.input,output:text,outputType:raw?.getClass()?.getName(),outputBytes:bytes.length,outputBase64:bytes.encodeBase64().toString(),outputSha256:hash,partRole:role,courseHeader:message.getHeaders().get('TRN_Course'),labVersionProperty:message.getProperty('TRN_LabVersion'),expectedRuntime:c.expectedRuntime ?: 'COMPLETED']
    def log=messageLogFactory.getMessageLog(message)
    if(log!=null) {
        log.addCustomHeaderProperty('TRN_TestResult',result.status.toString())
        log.addCustomHeaderProperty('TRN_OutputSha256',hash)
        if(text.size()<=200) log.addCustomHeaderProperty('TRN_OutputPreview',text.replaceAll('[\\x00-\\x1F]',' '))
    }
    message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(result)))
    message.setHeader('Content-Type','application/json; charset=UTF-8')
    return message
}
