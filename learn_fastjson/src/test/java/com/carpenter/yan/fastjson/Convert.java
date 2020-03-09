package com.carpenter.yan.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Convert {
    @Test
    public void test1(){
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        String s1 = JSON.toJSONString(map);
        System.out.println(s1);

        Map<String, Object> map2 = JSON.parseObject(s1, new TypeReference<Map<String, Object>>(){});
        System.out.println(map2.get("key"));
    }
}
