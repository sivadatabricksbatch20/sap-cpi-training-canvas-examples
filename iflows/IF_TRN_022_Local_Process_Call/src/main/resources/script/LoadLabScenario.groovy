import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "PC22_DEFAULT", "input": "Caller body", "objective": "Local process replaces caller body", "variant": "base", "props": {"LocalGreeting": "Hello from local process"}, "exact": "Hello from local process"}, {"id": "PC22_UNICODE", "input": "Caller body", "objective": "Properties remain visible in local process", "variant": "base", "props": {"LocalGreeting": "Hello సాయి"}, "exact": "Hello సాయి"}, {"id": "PC22_EMPTY", "input": "Original body", "objective": "Local process can return empty text", "variant": "base", "props": {"LocalGreeting": ""}, "exact": ""}, {"id": "PC22_XML", "input": "Original body", "objective": "Return structured local response", "variant": "base", "props": {"LocalGreeting": "<result>Accepted</result>"}, "xmlRoot": "result", "contains": ["Accepted"]}]''')
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
