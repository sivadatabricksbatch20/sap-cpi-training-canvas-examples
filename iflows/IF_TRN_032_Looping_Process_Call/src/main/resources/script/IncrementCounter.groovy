import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper
Message processData(Message message) {
 int next=new XmlSlurper().parseText(message.getBody(String)).text().toInteger()+1
 message.setProperty('CounterValue',next)
 message.setBody('<counter>'+next+'</counter>')
 return message
}