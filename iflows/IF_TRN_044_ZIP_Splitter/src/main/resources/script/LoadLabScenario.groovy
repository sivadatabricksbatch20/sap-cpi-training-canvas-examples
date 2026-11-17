import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import java.util.UUID
Message processData(Message message) {
    def cases = new JsonSlurper().parseText('''[{"id": "ARC44_TWO", "input": "UEsDBBQAAAAAAFk1cV2LntnTAQAAAAEAAAAFAAAAYS50eHRBUEsDBBQAAAAAAFk1cV0xz9BKAQAAAAEAAAAFAAAAYi50eHRCUEsBAhQDFAAAAAAAWTVxXYue2dMBAAAAAQAAAAUAAAAAAAAAAAAAAIABAAAAAGEudHh0UEsBAhQDFAAAAAAAWTVxXTHP0EoBAAAAAQAAAAUAAAAAAAAAAAAAAIABJAAAAGIudHh0UEsFBgAAAAACAAIAZgAAAEgAAAAAAA==", "objective": "Two file archive", "variant": "base", "exact": "AB"}, {"id": "ARC44_ONE", "input": "UEsDBBQAAAAAAFk1cV0RwCFCAwAAAAMAAAAHAAAAb25lLnR4dE9uZVBLAQIUAxQAAAAAAFk1cV0RwCFCAwAAAAMAAAAHAAAAAAAAAAAAAACAAQAAAABvbmUudHh0UEsFBgAAAAABAAEANQAAACgAAAAAAA==", "objective": "One file archive", "variant": "base", "exact": "One"}, {"id": "ARC44_UTF8", "input": "UEsDBBQAAAAAAFk1cV1oGU6uBQAAAAUAAAAFAAAAYS50eHRKb3PDqVBLAwQUAAAAAABZNXFduNkSaAwAAAAMAAAABQAAAGIudHh04LC44LC+4LCv4LC/UEsBAhQDFAAAAAAAWTVxXWgZTq4FAAAABQAAAAUAAAAAAAAAAAAAAIABAAAAAGEudHh0UEsBAhQDFAAAAAAAWTVxXbjZEmgMAAAADAAAAAUAAAAAAAAAAAAAAIABKAAAAGIudHh0UEsFBgAAAAACAAIAZgAAAFcAAAAAAA==", "objective": "UTF-8 file contents", "variant": "base", "exact": "Joséసాయి"}, {"id": "ARC44_EMPTYFILE", "input": "UEsDBBQAAAAAAFk1cV0AAAAAAAAAAAAAAAAFAAAAYS50eHRQSwMEFAAAAAAAWTVxXTHP0EoBAAAAAQAAAAUAAABiLnR4dEJQSwECFAMUAAAAAABZNXFdAAAAAAAAAAAAAAAABQAAAAAAAAAAAAAAgAEAAAAAYS50eHRQSwECFAMUAAAAAABZNXFdMc/QSgEAAAABAAAABQAAAAAAAAAAAAAAgAEjAAAAYi50eHRQSwUGAAAAAAIAAgBmAAAARwAAAAAA", "objective": "An empty file plus a normal file", "variant": "base", "exact": "B"}, {"id": "ARC44_BAD", "input": "bm90IGFuIGFyY2hpdmU=", "objective": "Invalid archive rejected", "variant": "base", "negative": true}]''')
    String id = (message.getBody(String) ?: '').trim()
    def c = cases.find { it.id == id }
    if (c == null) throw new IllegalArgumentException('Unknown ScenarioId ' + id + '. Choose: ' + cases.collect {it.id}.join(', '))
    message.setProperty('TRN_Case', c)
    message.setProperty('Variant', c.variant ?: 'base')
    message.setProperty('EntryId', 'TRN_LAB_' + UUID.randomUUID().toString())
    (c.props ?: [:]).each { k,v -> message.setProperty(k,v) }
    (c.inputHeaders ?: [:]).each { k,v -> message.setHeader(k,v) }
    message.setHeader('TRN_ScenarioId',c.id)
    message.setBody(c.inputBinary ? c.inputBinary.decodeBase64() : c.input)
    def log=messageLogFactory.getMessageLog(message)
    if(log!=null) {
        log.addCustomHeaderProperty('TRN_ScenarioId',c.id)
        log.addCustomHeaderProperty('TRN_Expected',c.expectedRuntime ?: (c.negative ? 'FAILED' : 'COMPLETED'))
    }
    return message
}
