import com.sap.gateway.ip.core.customdev.util.Message
Message processData(Message message) {
 def log = messageLogFactory.getMessageLog(message)
 if(log != null) log.addCustomHeaderProperty("TRN_Result", message.getBody(String))
 return message
}