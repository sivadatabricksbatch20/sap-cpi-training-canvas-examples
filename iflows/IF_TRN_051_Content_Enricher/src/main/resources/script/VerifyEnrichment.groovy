import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper
Message processData(Message message) {
 def body=message.getBody(String)
 def xml=new XmlSlurper().parseText(body)
 def names=xml.depthFirst().findAll {it.name()=="name"}.collect {it.text()}
 if(!names.contains("Anu") || !names.contains("Sales")) throw new IllegalStateException("Enrichment must preserve Anu and add Sales: "+body)
 def log=messageLogFactory.getMessageLog(message)
 if(log!=null) log.addCustomHeaderProperty("TRN_Result",body)
 return message
}