import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "SP15_TWO", "input": "<orders><order><id>1</id></order><order><id>2</id></order></orders>", "objective": "Two order records", "variant": "base", "xmlValues": {"id": ["1", "2"]}}, {"id": "SP15_ONE", "input": "<orders><order><id>1</id></order></orders>", "objective": "Single order record", "variant": "base", "xmlValues": {"id": ["1"]}}, {"id": "SP15_STREAM", "input": "<orders><order><id>1</id></order><order><id>2</id></order><order><id>3</id></order><order><id>4</id></order><order><id>5</id></order><order><id>6</id></order><order><id>7</id></order><order><id>8</id></order><order><id>9</id></order><order><id>10</id></order><order><id>11</id></order><order><id>12</id></order><order><id>13</id></order><order><id>14</id></order><order><id>15</id></order><order><id>16</id></order><order><id>17</id></order><order><id>18</id></order><order><id>19</id></order><order><id>20</id></order></orders>", "objective": "Streaming larger order batch", "variant": "stream", "xmlValues": {"id": ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"]}}, {"id": "SP15_PARALLEL", "input": "<orders><order><id>1</id></order><order><id>2</id></order></orders>", "objective": "Parallel split; order-independent verification", "variant": "parallel", "xmlValuesUnordered": {"id": ["1", "2"]}}, {"id": "SP15_EMPTY", "input": "<orders/>", "objective": "Zero matching records: measure splitter behaviour", "variant": "base", "observe": true}, {"id": "SP15_BAD", "input": "<orders><order>", "objective": "Malformed XML rejected", "variant": "base", "negative": true}]''')
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
