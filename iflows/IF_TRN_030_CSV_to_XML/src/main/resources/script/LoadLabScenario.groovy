import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "CSV30_ROWS", "input": "1,Anu\\n2,Ravi", "objective": "Two rows to XML", "variant": "base", "xmlRoot": "students", "xmlValues": {"id": ["1", "2"], "name": ["Anu", "Ravi"]}}, {"id": "CSV30_UNICODE", "input": "3,José సాయి", "objective": "Unicode row", "variant": "base", "xmlValues": {"name": ["José సాయి"]}}, {"id": "CSV30_HEADER", "input": "id,name\\n1,Anu", "objective": "Skip incoming header", "variant": "header", "xmlValues": {"id": ["1"], "name": ["Anu"]}}, {"id": "CSV30_SEMI", "input": "1;Anu\\n2;Ravi", "objective": "Semicolon input", "variant": "semi", "xmlValues": {"id": ["1", "2"]}}, {"id": "CSV30_QUOTE", "input": "4,\\"Anu, Ravi\\"", "objective": "Quoted field contains delimiter", "variant": "base", "xmlValues": {"name": ["Anu, Ravi"]}}, {"id": "CSV30_EMPTY", "input": "5,", "objective": "Empty field", "variant": "base", "xmlRoot": "students"}]''')
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
