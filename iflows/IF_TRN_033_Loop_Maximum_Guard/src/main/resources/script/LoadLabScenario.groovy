import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "LOOP33_GUARD", "input": "<counter>0</counter>", "objective": "Loop condition never becomes false: maximum guard fails", "variant": "base", "negative": true, "props": {"CounterLimit": 5}}, {"id": "LOOP33_SKIP", "input": "<counter>2</counter>", "objective": "Do-while executes once, then condition becomes false", "variant": "base", "xmlLeafValues": ["3"], "props": {"CounterLimit": 2}}, {"id": "LOOP33_HIGH", "input": "<counter>10</counter>", "objective": "Do-while still executes once above threshold", "variant": "base", "xmlLeafValues": ["11"], "props": {"CounterLimit": 2}}, {"id": "LOOP33_ONE", "input": "<counter>1</counter>", "objective": "Persistent condition still fails at maximum", "variant": "base", "negative": true, "props": {"CounterLimit": 5}}]''')
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
