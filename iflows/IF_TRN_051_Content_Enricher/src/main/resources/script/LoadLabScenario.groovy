import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "CALL51_MATCH", "input": "<students><student><id>1</id><name>Anu</name></student></students>", "objective": "Matching lookup id", "variant": "base", "contains": ["Anu", "Sales"], "receiverCall": true}, {"id": "CALL51_UTF8", "input": "<students><student><id>1</id><name>సాయి</name></student></students>", "objective": "Unicode request preserved", "variant": "base", "contains": ["సాయి", "Sales"], "receiverCall": true}, {"id": "CALL51_NOMATCH", "input": "<students><student><id>999</id><name>Anu</name></student></students>", "objective": "No matching lookup key", "variant": "base", "receiverCall": true, "observe": true}, {"id": "CALL51_MULTI", "input": "<students><student><id>1</id><name>Anu</name></student><student><id>1</id><name>Ravi</name></student></students>", "objective": "Multiple request records", "variant": "base", "contains": ["Anu", "Ravi", "Sales"], "receiverCall": true}]''')
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
