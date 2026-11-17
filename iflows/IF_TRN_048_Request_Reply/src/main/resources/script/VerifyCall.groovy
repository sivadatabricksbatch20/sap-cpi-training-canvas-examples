import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper
Message processData(Message message) {
 String body=message.getBody(String)
 def xml=new XmlSlurper().parseText(body)
 if(xml.department.name.text() != "Sales") throw new IllegalStateException("Expected department reply, got "+body)
 def log=messageLogFactory.getMessageLog(message)
 if(log!=null) log.addCustomHeaderProperty("TRN_Result",body)
 return message
}