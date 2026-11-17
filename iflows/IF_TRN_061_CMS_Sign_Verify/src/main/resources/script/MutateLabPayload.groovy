import com.sap.gateway.ip.core.customdev.util.Message
Message processData(Message message) {
    def c=message.getProperty('TRN_Case')
    if(c.corrupt) message.setBody('NOT A VALID COMPRESSED ARCHIVE'.getBytes('UTF-8'))
    if(c.tamper) {
        String text=message.getBody(String)
        if(text?.contains('Anu') && text.contains('Signature')) message.setBody(text.replace('Anu','Ravi'))
        else if(text?.startsWith('-----BEGIN PGP MESSAGE-----')) {
            def lines=text.split('\\r?\\n',-1)
            int index=lines.findIndexOf {it.matches('[A-Za-z0-9+/]{40,}={0,2}')}
            if(index<0) throw new IllegalArgumentException('No armored PGP data line found')
            int at=(int)(lines[index].length()/2)
            def line=new StringBuilder(lines[index]);line.setCharAt(at, line.charAt(at)=='A' ? 'B' as char : 'A' as char);lines[index]=line.toString()
            message.setBody(lines.join('\n'))
        } else {
            byte[] bytes=message.getBody(byte[].class)
            byte[] changed=bytes.clone()
            byte[] needle=c.input.getBytes('UTF-8')
            int found=-1
            for(int i=0;i<=changed.length-needle.length;i++) {
                boolean match=true
                for(int j=0;j<needle.length;j++) if(changed[i+j]!=needle[j]) {match=false;break}
                if(match){found=i;break}
            }
            int pos=found>=0 ? found : Math.max(0,changed.length-16)
            changed[pos]=(byte)(changed[pos] ^ 0x01)
            message.setBody(changed)
        }
    }
    return message
}
