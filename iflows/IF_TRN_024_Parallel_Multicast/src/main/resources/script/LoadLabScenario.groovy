import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "MC24_BASE", "input": "<request/>", "objective": "Two service responses", "variant": "base", "props": {"BranchA": "Sales", "BranchB": "Finance"}, "xmlValuesUnordered": {"result": ["Sales", "Finance"]}}, {"id": "MC24_UNICODE", "input": "<request/>", "objective": "Unicode branch responses", "variant": "base", "props": {"BranchA": "José", "BranchB": "సాయి"}, "contains": ["José", "సాయి"]}, {"id": "MC24_EMPTY", "input": "<request/>", "objective": "Empty branch result", "variant": "base", "props": {"BranchA": "", "BranchB": "Finance"}, "contains": ["Finance"]}, {"id": "MC24_INVALID", "input": "<request/>", "objective": "Gather does not by itself guarantee well-formed XML", "variant": "base", "props": {"BranchA": "A & B", "BranchB": "Finance"}, "observe": true}]''')
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
