import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "SELECT43_TWO", "input": "<record>One</record>", "objective": "Select two dedicated records", "variant": "base", "props": {"SecondRecord": "<record>Two</record>"}, "storedValues": ["One", "Two"]}, {"id": "SELECT43_UNICODE", "input": "<record>José</record>", "objective": "Select Unicode records", "variant": "base", "props": {"SecondRecord": "<record>సాయి</record>"}, "storedValues": ["José", "సాయి"]}, {"id": "SELECT43_NUMERIC", "input": "<record>1001</record>", "objective": "Select numeric-looking text", "variant": "base", "props": {"SecondRecord": "<record>1002</record>"}, "storedValues": ["1001", "1002"]}]''')
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
