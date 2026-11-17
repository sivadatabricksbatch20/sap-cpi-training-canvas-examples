import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import groovy.util.XmlSlurper
Message processData(Message message) {
 def c=message.getProperty("TRN_Case")
 def raw=message.getBody()
 byte[] bytes=message.getBody(byte[].class)
 if(bytes==null) bytes=(raw==null ? "" : raw.toString()).getBytes(c.encoding ?: "UTF-8")
 String text=new String(bytes,c.encoding ?: "UTF-8")
 if(c.negative) throw new IllegalStateException("Expected native component rejection but it accepted "+c.id)
 if(c.containsKey("exact") && text!=c.exact) throw new IllegalStateException("Expected exact body "+c.exact+" but got "+text)
 if(c.expectedBase64 && text.replaceAll("\\s","")!=c.expectedBase64) throw new IllegalStateException("Base64 bytes mismatch")
 if(c.containsKey("expectedBase64") && c.expectedBase64=="" && text.trim()!="") throw new IllegalStateException("Expected empty encoding")
 if(c.expectedBinary && bytes.encodeBase64().toString()!=c.expectedBinary) throw new IllegalStateException("Decoded binary mismatch")
 def searchable=text
 if(c.xmlRoot){def xml=new XmlSlurper(false,true).parseText(text);if(xml.name()!=c.xmlRoot)throw new IllegalStateException("Wrong XML root "+xml.name());if(c.xmlNamespace && xml.namespaceURI()!=c.xmlNamespace)throw new IllegalStateException("Wrong XML namespace");searchable=xml.text()}
 if(c.jsonRoot || c.arrayPath || c.absentRoot){def obj=new JsonSlurper().parseText(text);if(c.jsonRoot && !obj.containsKey(c.jsonRoot))throw new IllegalStateException("Missing JSON key "+c.jsonRoot);if(c.absentRoot && obj.containsKey(c.absentRoot))throw new IllegalStateException("Root not suppressed");if(c.arrayPath){def v=obj;c.arrayPath.each {v=v[it]};if(!(v instanceof List))throw new IllegalStateException("Expected array at "+c.arrayPath)}}
 (c.contains ?: []).each {if(!searchable.contains(it))throw new IllegalStateException("Missing expected value "+it)}
 (c.excludes ?: []).each {if(searchable.contains(it))throw new IllegalStateException("Unexpected value "+it)}
 def result=[scenarioId:c.id,status:c.observe?"OBSERVED":"PASS",objective:c.objective,variant:c.variant,input:c.input,output:text,outputType:raw?.getClass()?.getName(),courseHeader:message.getHeaders().get("TRN_Course"),labVersionProperty:message.getProperty("TRN_LabVersion"),outputBytes:bytes.length,outputBase64:bytes.encodeBase64().toString()]
 def log=messageLogFactory.getMessageLog(message);if(log!=null){log.addCustomHeaderProperty("TRN_ScenarioId",c.id);log.addCustomHeaderProperty("TRN_TestResult",result.status.toString())}
 message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(result)));message.setHeader("Content-Type","application/json; charset=UTF-8")
 return message
}