package burp.u2c;

import burp.Getter;
import burp.IExtensionHelpers;
import burp.utils.Utils;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static burp.utils.Utils.stdout;

public class HttpMessageCharSet {
    public HttpMessageCharSet() {
    }

    private static String getSystemCharSet() {
        return Charset.defaultCharset().toString();
    }

    public static byte[] covertCharSetToByte(byte[] response) {
        String originalCharSet = getCharset(response);
        if (originalCharSet == null) {
            return response;
        } else {
            try {
                byte[] newResponse = (new String(response, originalCharSet)).getBytes(getSystemCharSet());
                return newResponse;
            } catch (UnsupportedEncodingException var4) {
                var4.printStackTrace(stdout);
                return response;
            }
        }
    }

    public static String getCharset(byte[] requestOrResponse) {
        IExtensionHelpers helpers = Utils.helpers;
        Getter getter = new Getter(helpers);
        boolean isRequest = !(new String(requestOrResponse)).startsWith("HTTP/");

        String contentType = getter.getHeaderValueOf(isRequest, requestOrResponse, "Content-Type");
        String tmpcharSet = "ISO-8859-1";
        if (contentType != null && contentType.toLowerCase().contains("charset=")) {
            tmpcharSet = contentType.toLowerCase().split("charset=")[1];
        }

        if (tmpcharSet == null) {
            CharsetDetector detector = new CharsetDetector();
            detector.setText(requestOrResponse);
            CharsetMatch cm = detector.detect();
            tmpcharSet = cm.getName();
        }

        tmpcharSet = tmpcharSet.toLowerCase().trim();
        List<String> commonCharSet = Arrays.asList("ASCII,ANSI,GBK,GB2312,UTF-8,GB18030,UNICODE,utf8".toLowerCase().split(","));
        Iterator var10 = commonCharSet.iterator();

        while (var10.hasNext()) {
            String item = (String) var10.next();
            if (tmpcharSet.contains(item)) {
                tmpcharSet = item;
            }
        }

        if (tmpcharSet.equals("utf8")) {
            tmpcharSet = "utf-8";
        }

        return tmpcharSet;
    }
}
