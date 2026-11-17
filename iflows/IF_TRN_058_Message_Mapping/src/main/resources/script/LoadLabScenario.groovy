import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "MM58_NAME", "input": "<student><name>Anu</name></student>", "objective": "Graphical field mapping", "variant": "base", "xmlRoot": "customer", "contains": ["Anu"]}, {"id": "MM58_UTF8", "input": "<student><name>సాయి</name></student>", "objective": "Unicode mapping", "variant": "base", "xmlRoot": "customer", "contains": ["సాయి"]}, {"id": "MM58_EMPTY", "input": "<student><name/></student>", "objective": "Empty source field", "variant": "base", "xmlRoot": "customer"}, {"id": "MM58_ESCAPE", "input": "<student><name>A &amp; B</name></student>", "objective": "Escaped XML data", "variant": "base", "xmlRoot": "customer", "contains": ["A & B"]}, {"id": "MM58_BAD", "input": "<student>", "objective": "Malformed XML rejected", "variant": "base", "negative": true}]''')
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
