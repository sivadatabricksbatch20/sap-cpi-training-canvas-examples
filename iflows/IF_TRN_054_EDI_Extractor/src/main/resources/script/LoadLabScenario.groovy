import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "EDI54_ORDER", "input": "UNA:+.? \'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1\'UNH+1+ORDERS:D:96A:UN\'BGM+220+PO123+9\'DTM+137:20261117:102\'UNT+4+1\'UNZ+1+1\'", "objective": "Extract EDIFACT envelope and message headers", "variant": "base", "headers": {"EDI_Document_Standard": "UN-EDIFACT", "EDI_Message_Type": "ORDERS", "EDI_Message_Release": "96A"}, "exact": "UNA:+.? \'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1\'UNH+1+ORDERS:D:96A:UN\'BGM+220+PO123+9\'DTM+137:20261117:102\'UNT+4+1\'UNZ+1+1\'"}, {"id": "EDI54_PARTNER", "input": "UNA:+.? \'UNB+UNOC:3+TRAININGA:14+TRAININGB:14+261117:1200+1\'UNH+1+ORDERS:D:96A:UN\'BGM+220+PO123+9\'DTM+137:20261117:102\'UNT+4+1\'UNZ+1+1\'", "objective": "Different partner identifiers", "variant": "base", "headers": {"EDI_Sender_ID": "TRAININGA", "EDI_Receiver_ID": "TRAININGB"}}, {"id": "EDI54_CONTROL", "input": "UNA:+.? \'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+123\'UNH+1+ORDERS:D:96A:UN\'BGM+220+PO123+9\'DTM+137:20261117:102\'UNT+4+1\'UNZ+1+123\'", "objective": "Interchange control number", "variant": "base", "headers": {"EDI_Interchange_control_number": "123"}}, {"id": "EDI54_BAD", "input": "NOT AN EDI MESSAGE", "objective": "Malformed EDI input", "variant": "base", "negative": true}]''')
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
