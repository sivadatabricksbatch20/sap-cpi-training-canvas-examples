import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "RT11_ORDER", "input": "<customer><name>Anu</name><amount>250</amount></customer>", "objective": "XML→JSON→XML preserves values", "variant": "base", "xmlEquivalent": "<customer><name>Anu</name><amount>250</amount></customer>"}, {"id": "RT11_REPEAT", "input": "<customer><name>Anu</name><item>A1</item><item>B2</item></customer>", "objective": "Repeated elements survive round trip", "variant": "base", "xmlLeafValues": ["Anu", "A1", "B2"]}, {"id": "RT11_UNICODE", "input": "<customer><name>José సాయి</name></customer>", "objective": "Unicode survives round trip", "variant": "base", "xmlLeafValues": ["José సాయి"]}, {"id": "RT11_EMPTY", "input": "<customer><name/></customer>", "objective": "Empty element representation", "variant": "base", "xmlRoot": "customer"}, {"id": "RT11_BAD", "input": "<customer>", "objective": "Malformed XML rejected", "variant": "base", "negative": true}]''')
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
