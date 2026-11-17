import com.sap.gateway.ip.core.customdev.util.Message
Message processData(Message message) { message.setProperty("TRN_NativeCallBody",message.getBody(String)); return message }