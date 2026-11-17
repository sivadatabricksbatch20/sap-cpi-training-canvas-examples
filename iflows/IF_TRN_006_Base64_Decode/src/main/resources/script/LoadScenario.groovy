import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
Message processData(Message message) {
 def catalog=new JsonSlurper().parseText('''[{"id": "BD01", "variant": "standard", "input": "SGVsbG8=", "objective": "Decode text", "exact": "Hello"}, {"id": "BD02", "variant": "standard", "input": "Sm9zw6kg4LC44LC+4LCv4LC/IPCfmIA=", "objective": "Decode UTF-8 Unicode", "exact": "José సాయి 😀"}, {"id": "BD03", "variant": "standard", "input": "bGluZTEKbGluZTINCmxpbmUz", "objective": "Restore line-ending bytes", "exact": "line1\\nline2\\r\\nline3"}, {"id": "BD04", "variant": "standard", "input": "", "objective": "Empty payload", "exact": ""}, {"id": "BD05", "variant": "standard", "input": "SGVs\\r\\nbG8=", "objective": "MIME-wrapped input", "exact": "Hello"}, {"id": "BD06", "variant": "standard", "input": "AAEC/w0K", "objective": "Binary file bytes including NUL", "expectedBinary": "AAEC/w0K"}, {"id": "BD07", "variant": "standard", "input": "SGVsbG8", "objective": "Missing padding behaviour", "exact": "Hello"}, {"id": "BD08", "variant": "standard", "input": "%%%not-base64%%%", "objective": "Invalid alphabet: measure actual decoder behaviour", "observe": true}, {"id": "BD09", "variant": "standard", "input": "A", "objective": "Invalid single-character input: decoder permissiveness", "observe": true, "exact": ""}, {"id": "BD10", "variant": "standard", "input": "%%%not-base64%%%", "objective": "Reject invalid Base64 with a strict input guard", "strict": true, "negative": true, "failureStage": "Load Scenario Fixture input guard"}, {"id": "BD11", "variant": "standard", "input": "SGVsbG8=", "objective": "Strict input guard accepts valid Base64", "strict": true, "exact": "Hello"}]''')
 def id=message.getBody(String).trim()
 def c=catalog.find {it.id==id}
 if(c==null) throw new IllegalArgumentException("Unknown ScenarioId "+id+". Choices: "+catalog.collect {it.id}.join(","))
 if(c.strict) java.util.Base64.getDecoder().decode(c.input)
 message.setProperty("TRN_Case",c);message.setProperty("Variant",c.variant)
 message.setHeader("TRN_ScenarioId",id)
 message.setBody(c.inputBinary ? c.inputBinary.decodeBase64() : c.input)
 return message
}