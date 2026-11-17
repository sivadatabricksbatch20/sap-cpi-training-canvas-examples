import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "HP46_DEFAULT", "input": "original", "objective": "Use header and exchange property", "variant": "base", "props": {"StudentName": "Anu"}, "inputHeaders": {"Course": "CPI"}, "exact": "Hello Anu, welcome to CPI!"}, {"id": "HP46_OTHER", "input": "original", "objective": "Change course header", "variant": "base", "props": {"StudentName": "Ravi"}, "inputHeaders": {"Course": "Integration Suite"}, "exact": "Hello Ravi, welcome to Integration Suite!"}, {"id": "HP46_UNICODE", "input": "original", "objective": "Unicode property", "variant": "base", "props": {"StudentName": "సాయి"}, "inputHeaders": {"Course": "CPI"}, "exact": "Hello సాయి, welcome to CPI!"}, {"id": "HP46_EMPTY", "input": "original", "objective": "Empty optional student name", "variant": "base", "props": {"StudentName": ""}, "inputHeaders": {"Course": "CPI"}, "exact": "Hello , welcome to CPI!"}]''')
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
