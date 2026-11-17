import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "ROUTE23_HIGH", "input": "<order><amount>1500</amount></order>", "objective": "Above threshold → priority", "variant": "base", "exact": "PRIORITY"}, {"id": "ROUTE23_BOUNDARY", "input": "<order><amount>1000</amount></order>", "objective": "Boundary is standard", "variant": "base", "exact": "STANDARD"}, {"id": "ROUTE23_LOW", "input": "<order><amount>0</amount></order>", "objective": "Zero amount → standard", "variant": "base", "exact": "STANDARD"}, {"id": "ROUTE23_MISSING", "input": "<order/>", "objective": "Missing optional amount → default route", "variant": "base", "exact": "STANDARD"}, {"id": "ROUTE23_DECIMAL", "input": "<order><amount>1000.01</amount></order>", "objective": "Decimal over threshold", "variant": "base", "exact": "PRIORITY"}, {"id": "ROUTE23_BAD", "input": "<order>", "objective": "Malformed XML rejected", "variant": "base", "negative": true}]''')
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
