import com.sap.gateway.ip.core.customdev.util.Message
import java.util.Locale

Message processData(Message message) {
    String input = message.getBody(String)
    message.setBody(input.toUpperCase(Locale.ROOT))
    return message
}
