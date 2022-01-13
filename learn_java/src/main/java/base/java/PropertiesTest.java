package base.java;

import org.junit.Test;

import java.util.Properties;

public class PropertiesTest {
    @Test
    public void testPutOrder(){
        Properties p = new Properties();
        p.put("abc", "1");
        p.put("abc", "2");
        System.out.println(p.get("abc"));
    }
}
