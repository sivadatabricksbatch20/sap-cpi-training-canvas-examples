import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "LINE16_LF", "input": "Anu\\nRavi", "objective": "LF records", "variant": "base", "exact": "AnuRavi"}, {"id": "LINE16_CRLF", "input": "Anu\\r\\nRavi", "objective": "CRLF records", "variant": "base", "exact": "AnuRavi"}, {"id": "LINE16_ONE", "input": "Anu", "objective": "One record", "variant": "base", "exact": "Anu"}, {"id": "LINE16_UNICODE", "input": "José\\nసాయి", "objective": "Unicode lines", "variant": "base", "exact": "Joséసాయి"}, {"id": "LINE16_EMPTY", "input": "Anu\\n\\nRavi", "objective": "Empty line handling", "variant": "base", "observe": true}]''')
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
