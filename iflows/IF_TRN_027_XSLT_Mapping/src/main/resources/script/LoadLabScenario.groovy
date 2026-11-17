import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "XSLT27_NAME", "input": "<student><name>Anu</name></student>", "objective": "Template and value-of", "variant": "base", "xmlRoot": "greeting", "contains": ["Hello, Anu!"]}, {"id": "XSLT27_UNICODE", "input": "<student><name>José సాయి</name></student>", "objective": "Unicode text", "variant": "base", "xmlRoot": "greeting", "contains": ["Hello, José సాయి!"]}, {"id": "XSLT27_ESCAPE", "input": "<student><name>A &amp; B</name></student>", "objective": "XML escaping", "variant": "base", "xmlRoot": "greeting", "contains": ["Hello, A & B!"]}, {"id": "XSLT27_MISSING", "input": "<student/>", "objective": "Missing name produces empty substitution", "variant": "base", "xmlRoot": "greeting", "contains": ["Hello, !"]}, {"id": "XSLT27_BAD", "input": "<student>", "objective": "Malformed XML rejected", "variant": "base", "negative": true}]''')
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
