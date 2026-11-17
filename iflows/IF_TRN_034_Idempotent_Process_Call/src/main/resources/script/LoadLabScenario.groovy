import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "IDEMP34_FIRST", "input": "Hello from main", "objective": "First occurrence executes local process", "variant": "base", "props": {"SourceId": "TRN034_REVIEW_A"}, "oneOf": ["Hello from main", "Hello from local process"]}, {"id": "IDEMP34_REPEAT", "input": "Hello from main", "objective": "Repeat same key skips local call", "variant": "base", "props": {"SourceId": "TRN034_REVIEW_A"}, "oneOf": ["Hello from main", "Hello from local process"]}, {"id": "IDEMP34_NEW", "input": "Hello from main", "objective": "Different key has its own first run", "variant": "base", "props": {"SourceId": "TRN034_REVIEW_B"}, "oneOf": ["Hello from main", "Hello from local process"]}]''')
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
