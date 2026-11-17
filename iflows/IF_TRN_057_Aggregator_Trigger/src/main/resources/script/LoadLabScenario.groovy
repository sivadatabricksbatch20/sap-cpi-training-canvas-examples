import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "TRIGGER57_ORDERED", "input": "<order><batch>TRN053</batch><sequence>1</sequence><last>false</last></order>", "objective": "Send sequences one then two", "variant": "base", "props": {"SecondOrder": "<order><batch>TRN053</batch><sequence>2</sequence><last>true</last></order>"}, "observe": true}, {"id": "TRIGGER57_REVERSED", "input": "<order><batch>TRN053_REV</batch><sequence>2</sequence><last>true</last></order>", "objective": "Out-of-order delivery with sequence two correctly marked as last", "variant": "base", "props": {"SecondOrder": "<order><batch>TRN053_REV</batch><sequence>1</sequence><last>false</last></order>"}, "observe": true}, {"id": "TRIGGER57_VERIFY", "input": "Verify latest aggregated message", "objective": "Read/delete aggregated XML and assert ordered sequence values", "variant": "verify", "xmlValues": {"sequence": ["1", "2"]}}, {"id": "TRIGGER57_TIMEOUT", "input": "<order><batch>TRN053_TIMEOUT</batch><sequence>1</sequence><last>false</last></order>", "objective": "Send an incomplete group; wait for configured timeout", "variant": "single", "observe": true}, {"id": "TRIGGER57_VERIFY_TIMEOUT", "input": "Verify incomplete group", "objective": "After timeout, assert the one-message result", "variant": "verify", "xmlValues": {"sequence": ["1"]}}, {"id": "TRIGGER57_BAD_LAST", "input": "<order><batch>TRN053_REV</batch><sequence>2</sequence><last>false</last></order>", "objective": "Reject a last-message marker below an already accepted sequence", "variant": "base", "props": {"SecondOrder": "<order><batch>TRN053_REV</batch><sequence>1</sequence><last>true</last></order>"}, "negative": true}]''')
    String id = (message.getBody(String) ?: '').trim()
    def c = cases.find { it.id == id }
    if (c == null) throw new IllegalArgumentException('Unknown ScenarioId ' + id + '. Choose: ' + cases.collect {it.id}.join(', '))
    if(c.input?.contains('TRN053')) {
        String batch='TRN053_'+UUID.randomUUID().toString()
        c.input=c.input.replace('TRN053',batch)
        (c.props ?: [:]).each {k,v -> if(v instanceof String) c.props[k]=v.replace('TRN053',batch)}
    }
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
