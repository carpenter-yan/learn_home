package base.java;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static java.lang.System.out;

public class URLDecoderTest {
    @Test
    public void test1(){
        try {
            String url = URLEncoder.encode("疯狂Java讲义123", "GBK");
            out.println("encode:" + url);
            //%E7%96%AF%E7%8B%82Java%E8%AE%B2%E4%B9%89123   utf-8
            //%B7%E8%BF%F1Java%BD%B2%D2%E5123   GBK
            String urlDecode = URLDecoder.decode(url, "GBK");
            out.println("decode:" + urlDecode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
