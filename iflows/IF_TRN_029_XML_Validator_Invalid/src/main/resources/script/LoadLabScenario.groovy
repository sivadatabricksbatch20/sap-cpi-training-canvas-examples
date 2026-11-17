import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "VAL29_TEXT", "input": "<student><id>ABC</id><name>Anu</name></student>", "objective": "Text identifier rejected", "variant": "base", "negative": true}, {"id": "VAL29_VALID", "input": "<student><id>1</id><name>Anu</name></student>", "objective": "Positive integer accepted", "variant": "base", "exact": "<student><id>1</id><name>Anu</name></student>"}, {"id": "VAL29_UNICODE", "input": "<student><id>2</id><name>సాయి</name></student>", "objective": "Unicode name accepted", "variant": "base", "xmlRoot": "student", "contains": ["సాయి"]}, {"id": "VAL29_ZERO", "input": "<student><id>0</id><name>Anu</name></student>", "objective": "Zero is not a positive integer", "variant": "base", "negative": true}, {"id": "VAL29_MISSING", "input": "<student><id>1</id></student>", "objective": "Required name missing", "variant": "base", "negative": true}, {"id": "VAL29_ORDER", "input": "<student><name>Anu</name><id>1</id></student>", "objective": "XSD sequence order enforced", "variant": "base", "negative": true}]''')
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
