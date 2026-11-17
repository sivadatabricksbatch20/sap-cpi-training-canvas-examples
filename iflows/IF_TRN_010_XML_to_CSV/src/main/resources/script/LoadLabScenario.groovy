import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "CSV10_ROWS", "input": "<students><student><id>1</id><name>Anu</name></student><student><id>2</id><name>Ravi</name></student></students>", "objective": "Two records to comma CSV", "variant": "base", "normalizedLines": "1,Anu\\n2,Ravi"}, {"id": "CSV10_UNICODE", "input": "<students><student><id>3</id><name>José సాయి</name></student></students>", "objective": "Unicode data", "variant": "base", "normalizedLines": "3,José సాయి"}, {"id": "CSV10_SEMI", "input": "<students><student><id>1</id><name>Anu</name></student><student><id>2</id><name>Ravi</name></student></students>", "objective": "Semicolon separator", "variant": "semi", "normalizedLines": "1;Anu\\n2;Ravi"}, {"id": "CSV10_PIPE", "input": "<students><student><id>1</id><name>Anu</name></student><student><id>2</id><name>Ravi</name></student></students>", "objective": "Pipe separator", "variant": "pipe", "normalizedLines": "1|Anu\\n2|Ravi"}, {"id": "CSV10_HEADER", "input": "<students><student><id>1</id><name>Anu</name></student><student><id>2</id><name>Ravi</name></student></students>", "objective": "XML element names as header", "variant": "header", "contains": ["id,name", "1,Anu", "2,Ravi"]}, {"id": "CSV10_QUOTE", "input": "<students><student><id>4</id><name>Anu, Ravi</name></student></students>", "objective": "Field containing separator is quoted", "variant": "base", "contains": ["Anu, Ravi"]}, {"id": "CSV10_BAD", "input": "<students><student>", "objective": "Malformed XML rejected", "variant": "base", "negative": true}]''')
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
