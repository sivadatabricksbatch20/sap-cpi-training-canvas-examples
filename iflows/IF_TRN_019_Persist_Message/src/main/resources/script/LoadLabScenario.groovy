import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "PERSIST19_XML", "input": "<order><id>SO1001</id></order>", "objective": "Persist an XML snapshot", "variant": "base", "exact": "<order><id>SO1001</id></order>"}, {"id": "PERSIST19_JSON", "input": "{\\"orderId\\":\\"SO1002\\"}", "objective": "Persist JSON snapshot", "variant": "base", "exact": "{\\"orderId\\":\\"SO1002\\"}"}, {"id": "PERSIST19_UNICODE", "input": "José సాయి", "objective": "Persist Unicode snapshot", "variant": "base", "exact": "José సాయి"}, {"id": "PERSIST19_MULTI", "input": "line1\\nline2", "objective": "Persist line endings", "variant": "base", "exact": "line1\\nline2"}]''')
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
