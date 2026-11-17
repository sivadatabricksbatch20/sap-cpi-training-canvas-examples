import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper
Message processData(Message message) {
 String body=message.getBody(String)
 def xml=new XmlSlurper().parseText(body)
 if(xml.student.name.text() != "Anu") throw new IllegalStateException("Expected original Send body, got "+body)
 def log=messageLogFactory.getMessageLog(message)
 if(log!=null) log.addCustomHeaderProperty("TRN_Result",body)
 return message
}