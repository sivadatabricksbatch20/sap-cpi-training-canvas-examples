import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "EX36_TEXT", "input": "<student><id>ABC</id><name>Anu</name></student>", "objective": "Catch XML validation failure", "variant": "base", "exact": "Handled validation error", "expectedRuntime": "FAILED", "handled": true}, {"id": "EX36_ZERO", "input": "<student><id>0</id><name>Anu</name></student>", "objective": "Catch another schema error", "variant": "base", "exact": "Handled validation error", "expectedRuntime": "FAILED", "handled": true}, {"id": "EX36_VALID", "input": "<student><id>1</id><name>Anu</name></student>", "objective": "Valid input bypasses error handler", "variant": "base", "exact": "<student><id>1</id><name>Anu</name></student>"}, {"id": "EX36_XML", "input": "<student>", "objective": "Catch malformed XML", "variant": "base", "exact": "Handled validation error", "handled": true, "expectedRuntime": "FAILED"}]''')
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
