import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "PARAM59_DEFAULT", "input": "", "objective": "Externalized greeting prefix and scenario", "variant": "base", "props": {"StudentName": "Anu"}, "greeting": true}, {"id": "PARAM59_UTF8", "input": "", "objective": "Unicode student", "variant": "base", "props": {"StudentName": "సాయి"}, "greeting": true}, {"id": "PARAM59_EMPTY", "input": "", "objective": "Empty optional student", "variant": "base", "props": {"StudentName": ""}, "greeting": true}, {"id": "PARAM59_OTHER", "input": "", "objective": "Another configured scenario", "variant": "base", "props": {"StudentName": "Ravi"}, "greeting": true}]''')
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
