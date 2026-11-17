import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "SEC63_XML", "input": "<student><name>Anu</name></student>", "objective": "Sign/verify XML", "variant": "base", "xmlEquivalent": "<student><name>Anu</name></student>"}, {"id": "SEC63_UTF8", "input": "<student><name>సాయి</name></student>", "objective": "Unicode signed XML", "variant": "base", "xmlLeafValues": ["సాయి"]}, {"id": "SEC63_TAMPER", "input": "<student><name>Anu</name></student>", "objective": "Tampered XML fails verification", "variant": "base", "tamper": true, "negative": true}, {"id": "SEC63_BAD", "input": "<student>", "objective": "Malformed XML rejected before signing", "variant": "base", "negative": true}]''')
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
