import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper
Message processData(Message message) {
 def xml=new XmlSlurper().parseText(message.getBody(String))
 def values=xml.depthFirst().findAll {it.name()=="sequence"}.collect {it.text()}
 if(values != ["1","2"]) throw new IllegalStateException("Expected ordered sequences 1,2; got "+values)
 def log=messageLogFactory.getMessageLog(message)
 if(log!=null) log.addCustomHeaderProperty("TRN_Sequences",values.join(","))
 return message
}