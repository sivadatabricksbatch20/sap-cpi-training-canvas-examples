import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "TIMER21_START", "input": "", "objective": "Once on deployment", "variant": "base", "props": {"StudentName": "Anu", "EventTime": "2026-11-17T09:00:00Z"}, "exact": "Timer event for Anu at 2026-11-17T09:00:00Z"}, {"id": "TIMER21_REDEPLOY", "input": "", "objective": "Redeploy to produce a second one-time run", "variant": "base", "props": {"StudentName": "Ravi", "EventTime": "2026-11-17T09:01:00Z"}, "exact": "Timer event for Ravi at 2026-11-17T09:01:00Z"}, {"id": "TIMER21_UTC", "input": "", "objective": "Use an explicit UTC event timestamp", "variant": "base", "props": {"StudentName": "సాయి", "EventTime": "2026-11-17T00:00:00Z"}, "exact": "Timer event for సాయి at 2026-11-17T00:00:00Z"}]''')
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
