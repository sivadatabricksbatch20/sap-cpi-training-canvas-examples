import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "EDI56_NO_SCHEMA", "input": "UNA:+.? \'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1\'UNH+1+ORDERS:D:96A:UN\'BGM+220+PO123+9\'DTM+137:20261117:102\'UNT+4+1\'UNZ+1+1\'", "objective": "Prerequisite diagnostic: missing licensed runtime XSD is rejected; this does not test successful EDI conversion", "variant": "base", "negative": true, "prerequisiteCheck": true}, {"id": "EDI56_ORDER", "input": "UNA:+.? \'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1\'UNH+1+ORDERS:D:96A:UN\'BGM+220+PO123+9\'DTM+137:20261117:102\'UNT+4+1\'UNZ+1+1\'", "objective": "EDIFACT order to XML and back", "variant": "base", "contains": ["PO123"], "blocked": "A licensed UN/EDIFACT ORDERS D96A Integration Advisor runtime XSD is required. SAP currently reports: You do not have valid license for the type system."}, {"id": "EDI56_OTHER", "input": "UNA:+.? \'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1\'UNH+1+ORDERS:D:96A:UN\'BGM+220+PO999+9\'DTM+137:20261117:102\'UNT+4+1\'UNZ+1+1\'", "objective": "Changed purchase-order reference", "variant": "base", "contains": ["PO999"], "blocked": "A licensed UN/EDIFACT ORDERS D96A Integration Advisor runtime XSD is required. SAP currently reports: You do not have valid license for the type system."}, {"id": "EDI56_BAD", "input": "NOT AN EDI MESSAGE", "objective": "Malformed input is rejected before any EDI conversion can complete", "variant": "base", "negative": true, "blocked": "A licensed UN/EDIFACT ORDERS D96A Integration Advisor runtime XSD is required. SAP currently reports: You do not have valid license for the type system."}]''')
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
