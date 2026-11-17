import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "H01", "input": "", "objective": "Build a greeting from properties", "variant": "base", "props": {"StudentName": "student", "GreetingPrefix": "Hello"}, "exact": "Hello, student!"}, {"id": "H02", "input": "", "objective": "Header/property substitution", "variant": "base", "props": {"StudentName": "Ravi", "GreetingPrefix": "Welcome"}, "exact": "Welcome, Ravi!"}, {"id": "H03", "input": "", "objective": "Unicode greeting", "variant": "base", "props": {"StudentName": "సాయి", "GreetingPrefix": "Hello"}, "exact": "Hello, సాయి!"}, {"id": "H04", "input": "", "objective": "Empty optional name", "variant": "base", "props": {"StudentName": "", "GreetingPrefix": "Hello"}, "exact": "Hello, !"}]''')
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
