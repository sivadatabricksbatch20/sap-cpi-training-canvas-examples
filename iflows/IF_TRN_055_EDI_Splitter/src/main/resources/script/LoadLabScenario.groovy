import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "EDI55_TWO", "input": "UNA:+.? \'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+2\'UNH+1+ORDERS:D:96A:UN\'BGM+220+PO123+9\'DTM+137:20261117:102\'UNT+4+1\'UNH+2+ORDERS:D:96A:UN\'BGM+220+PO124+9\'DTM+137:20261117:102\'UNT+4+2\'UNZ+2+2\'", "objective": "Two messages split and gathered", "variant": "base", "contains": ["PO123", "PO124"]}, {"id": "EDI55_ONE", "input": "UNA:+.? \'UNB+UNOC:3+SENDER:14+RECEIVER:14+261117:1200+1\'UNH+1+ORDERS:D:96A:UN\'BGM+220+PO123+9\'DTM+137:20261117:102\'UNT+4+1\'UNZ+1+1\'", "objective": "Single message", "variant": "base", "contains": ["PO123"]}, {"id": "EDI55_PARTNER", "input": "UNA:+.? \'UNB+UNOC:3+TRAININGA:14+RECEIVER:14+261117:1200+2\'UNH+1+ORDERS:D:96A:UN\'BGM+220+PO123+9\'DTM+137:20261117:102\'UNT+4+1\'UNH+2+ORDERS:D:96A:UN\'BGM+220+PO124+9\'DTM+137:20261117:102\'UNT+4+2\'UNZ+2+2\'", "objective": "Partner-specific envelope preserved", "variant": "base", "contains": ["TRAININGA", "PO123", "PO124"]}, {"id": "EDI55_BAD", "input": "NOT AN EDI MESSAGE", "objective": "Invalid EDI input", "variant": "base", "negative": true}]''')
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
