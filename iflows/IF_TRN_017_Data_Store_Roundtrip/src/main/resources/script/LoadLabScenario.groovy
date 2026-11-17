import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "DS17_XML", "input": "<student><id>1</id><name>Anu</name></student>", "objective": "Write, get and delete own entry", "variant": "base", "exact": "<student><id>1</id><name>Anu</name></student>"}, {"id": "DS17_UNICODE", "input": "José సాయి", "objective": "UTF-8 stored value", "variant": "base", "exact": "José సాయి"}, {"id": "DS17_JSON", "input": "{\\"orderId\\":\\"SO1001\\",\\"amount\\":250}", "objective": "Store and retrieve JSON bytes", "variant": "base", "exact": "{\\"orderId\\":\\"SO1001\\",\\"amount\\":250}"}, {"id": "DS17_DELETEGET", "input": "<student>Anu</student>", "objective": "Get with delete-on-read", "variant": "deleteget", "exact": "<student>Anu</student>"}]''')
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
