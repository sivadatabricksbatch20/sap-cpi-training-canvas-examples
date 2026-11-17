import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "HASH13_SHA256", "input": "Order SO1001", "objective": "Digest SHA-256", "variant": "SHA-256", "headers": {"SAPMessageDigest": "pblmxSeVEKDDCJ6XBgVppoxSakIJFhScbIAcYhtShK4="}, "exact": "Order SO1001"}, {"id": "HASH13_SHA384", "input": "Order SO1001", "objective": "Digest SHA-384", "variant": "SHA-384", "headers": {"SAPMessageDigest": "gwAj9eB/9teFnx5PdQPBPHcnCjFED1FGP4/QOmjg34JlKGg5p8JYrg0sMJ0tC3uk"}, "exact": "Order SO1001"}, {"id": "HASH13_SHA512", "input": "Order SO1001", "objective": "Digest SHA-512", "variant": "SHA-512", "headers": {"SAPMessageDigest": "IQ0KBb7h6nsm9Qg8Q9MXmUprhN/w/3Gn/09cJRnidQhn0wXWG1KRuyq76S+O7KfPd7sTWRkSlTzzIZpHjvpcNA=="}, "exact": "Order SO1001"}, {"id": "HASH13_SHA1", "input": "Order SO1001", "objective": "Digest SHA-1 (legacy demonstration only)", "variant": "SHA-1", "headers": {"SAPMessageDigest": "XO7LYqMDMrSlgNH5qYiC9NaHcCE="}, "exact": "Order SO1001"}, {"id": "HASH13_MD5", "input": "Order SO1001", "objective": "Digest MD5 (legacy demonstration only)", "variant": "MD5", "headers": {"SAPMessageDigest": "luDV5IRQeZkG2PdCtrWB+Q=="}, "exact": "Order SO1001"}, {"id": "HASH13_EDGE0", "input": "", "objective": "SHA-256 exact UTF-8 bytes", "variant": "SHA-256", "headers": {"SAPMessageDigest": "47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU="}, "exact": ""}, {"id": "HASH13_EDGE1", "input": "José సాయి", "objective": "SHA-256 exact UTF-8 bytes", "variant": "SHA-256", "headers": {"SAPMessageDigest": "DswVv4bLLgYM3YxwTYGuKsu3q9FMc6kNydYNVAJu2NE="}, "exact": "José సాయి"}, {"id": "HASH13_EDGE2", "input": "line1\\nline2", "objective": "SHA-256 exact UTF-8 bytes", "variant": "SHA-256", "headers": {"SAPMessageDigest": "aDN24pCCm0gsJlV0XK/6eh3M+hCvqmLawrQt1saND4M="}, "exact": "line1\\nline2"}]''')
    String id = (message.getBody(String) ?: '').trim()
    def c = cases.find { it.id == id }
    if (c == null) throw new IllegalArgumentException('Unknown ScenarioId ' + id + '. Choose: ' + cases.collect {it.id}.join(', '))
    message.setProperty('TRN_Case', c)
    message.setProperty('Variant', c.variant ?: 'base')
    message.setProperty('EntryId', 'TRN_LAB_' + UUID.randomUUID().toString())
    (c.props ?: [:]).each { k,v -> message.setProperty(k,v) }
    (c.inputHeaders ?: [:]).each { k,v -> message.setHeader(k,v) }
    message.setHeader('TRN_ScenarioId',c.id)
    message.setBody(c.inputBinary ? c.inputBinary.decodeBase64() : c.input)
    def log=messageLogFactory.getMessageLog(message)
    if(log!=null) {
        log.addCustomHeaderProperty('TRN_ScenarioId',c.id)
        log.addCustomHeaderProperty('TRN_Expected',c.expectedRuntime ?: (c.negative ? 'FAILED' : 'COMPLETED'))
    }
    return message
}
