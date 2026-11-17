import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "SEC65_TEXT", "input": "Order SO1001, amount 250", "objective": "Security processing of business text", "variant": "base", "cmsParts": true}, {"id": "SEC65_UTF8", "input": "José సాయి 😀", "objective": "UTF-8 security payload", "variant": "base", "cmsParts": true}, {"id": "SEC65_JSON", "input": "{\\"orderId\\":\\"SO1001\\"}", "objective": "Structured JSON payload", "variant": "base", "cmsParts": true}, {"id": "SEC65_MULTI", "input": "line1\\nline2", "objective": "Preserve message bytes", "variant": "base", "cmsParts": true}]''')
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
