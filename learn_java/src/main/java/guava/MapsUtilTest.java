package guava;

import com.google.common.collect.Maps;

import java.util.Map;

public class MapsUtilTest {

    public static void main(String[] args) {
        Map<String, String> data = (Map<String, String>) Maps.newHashMap().put("data", "1111");
        System.out.println(data.get("data"));
    }
}
