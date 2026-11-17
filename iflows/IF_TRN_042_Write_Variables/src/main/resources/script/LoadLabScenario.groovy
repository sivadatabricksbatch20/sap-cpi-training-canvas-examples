import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "VAR42_TEXT", "input": "variable demo", "objective": "Write/read local variable", "variant": "base", "props": {"PersistedGreeting": "Hello from a persisted variable"}, "exact": "Hello from a persisted variable"}, {"id": "VAR42_UNICODE", "input": "variable demo", "objective": "Persist Unicode", "variant": "base", "props": {"PersistedGreeting": "Hello సాయి"}, "exact": "Hello సాయి"}, {"id": "VAR42_JSON", "input": "variable demo", "objective": "Persist structured text", "variant": "base", "props": {"PersistedGreeting": "{\\"status\\":\\"accepted\\"}"}, "exact": "{\\"status\\":\\"accepted\\"}"}, {"id": "VAR42_REPLACE", "input": "variable demo", "objective": "Overwrite own variable", "variant": "base", "props": {"PersistedGreeting": "Updated greeting"}, "exact": "Updated greeting"}]''')
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
