import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "SCRIPT25_TEXT", "input": "Hello, student!", "objective": "Uppercase body", "variant": "base", "exact": "HELLO, STUDENT!"}, {"id": "SCRIPT25_UNICODE", "input": "José సాయి ß", "objective": "Unicode casing", "variant": "base", "exact": "JOSÉ సాయి SS"}, {"id": "SCRIPT25_EMPTY", "input": "", "objective": "Empty body", "variant": "base", "exact": ""}, {"id": "SCRIPT25_MULTI", "input": "Line one\\nline two", "objective": "Preserve line separators", "variant": "base", "exact": "LINE ONE\\nLINE TWO"}, {"id": "SCRIPT25_JSON", "input": "{\\"status\\":\\"new\\"}", "objective": "Whole-body casing also changes JSON keys/values", "variant": "base", "exact": "{\\"STATUS\\":\\"NEW\\"}"}]''')
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
