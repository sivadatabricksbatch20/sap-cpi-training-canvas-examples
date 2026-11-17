importClass(com.sap.gateway.ip.core.customdev.util.Message);
function processData(message) {
    var input = String(message.getBody(java.lang.String));
    message.setBody(input.toUpperCase());
    return message;
}
