import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.util.XmlSlurper
Message processData(Message message) {
 def catalog=new JsonSlurper().parseText('''[{"id": "MOCK49_SALES", "input": "<students><student><id>1</id></student></students>", "objective": "Sales lookup request", "variant": "base", "contains": ["Sales"]}, {"id": "MOCK49_UNKNOWN", "input": "<students><student><id>999</id></student></students>", "objective": "Unknown key response", "variant": "base", "observe": true}, {"id": "MOCK49_EMPTY", "input": "<students/>", "objective": "Empty request response", "variant": "base", "observe": true}]''')
 def selection=message.getProperty('ScenarioId')
 String body=message.getBody(String)
 if(selection!='LIVE') {
  def c=catalog.find {it.id==selection}
  if(c==null) throw new IllegalArgumentException('Choose LIVE or a documented MOCK49 scenario')
  body=c.input
 }
 def xml=new XmlSlurper(false,true).parseText(body)
 def ids=xml.depthFirst().findAll {it.name()=='id'}.collect {it.text()}.unique()
 def records=[]
 if(ids.contains('1')) records.add('<department><id>1</id><name>Sales</name></department>')
 if(ids.contains('2')) records.add('<department><id>2</id><name>Finance</name></department>')
 def response='<lookup>'+records.join('')+'</lookup>'
 def parsed=new XmlSlurper().parseText(response)
 if(parsed.department.size()!=records.size()) throw new IllegalStateException('Mock response cardinality mismatch')
 def id=ids.contains('1') ? 'MOCK49_SALES' : ids ? 'MOCK49_UNKNOWN' : 'MOCK49_EMPTY'
 def log=messageLogFactory.getMessageLog(message)
 if(log!=null) {log.addCustomHeaderProperty('TRN_ScenarioId',id);log.addCustomHeaderProperty('TRN_TestResult','PASS');log.addCustomHeaderProperty('TRN_OutputPreview',response.take(200))}
 message.setBody(response)
 return message
}