import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "XML09_REMOVE", "input": "<?xml version=\\"1.0\\"?><student><name>Anu</name></student>", "objective": "Remove declaration", "variant": "base", "exact": "<student><name>Anu</name></student>"}, {"id": "XML09_KEEP", "input": "<?xml version=\\"1.0\\"?><student><name>Anu</name></student>", "objective": "Keep declaration", "variant": "keep", "contains": ["<?xml", "Anu"]}, {"id": "XML09_UNICODE", "input": "<student><name>José సాయి</name></student>", "objective": "Preserve Unicode and valid XML", "variant": "base", "xmlRoot": "student", "contains": ["José సాయి"]}, {"id": "XML09_CONTROL", "input": "<student>A\\u0001B</student>", "objective": "Remove invalid XML control character", "variant": "clean", "exact": "<student>AB</student>"}, {"id": "XML09_SUB", "input": "<student>A\\u0001B</student>", "objective": "Substitute invalid XML control character", "variant": "substitute", "contains": ["A", "B"], "excludes": ["\\u0001"]}, {"id": "XML09_DTD", "input": "<!DOCTYPE student SYSTEM \\"urn:training:never-fetch\\"><student>Anu</student>", "objective": "Remove external DTD without fetching it", "variant": "dtd", "contains": ["Anu"], "excludes": ["DOCTYPE"]}]''')
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
