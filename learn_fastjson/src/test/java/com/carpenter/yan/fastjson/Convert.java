package com.carpenter.yan.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

    @Test
    public void test2(){
        String str = "{\"cron\":\"0 12 ? ? ? ?\",\"activity\":\"[{\\\"htmlAnswer\\\":\\\"为您准备的活动优惠，咨询客服了解更多~\\\",\\\"activityUrl\\\":\\\"\\\",\\\"activityEndTime\\\":\\\"\\\",\\\"simpleAnswer\\\":\\\"为您准备的活动优惠，咨询客服了解更多~\\\",\\\"activityBeginTime\\\":\\\"\\\",\\\"imageUrl\\\":\\\"\\\",\\\"activityName\\\":\\\"\\\",\\\"order\\\":1},{\\\"htmlAnswer\\\":\\\"为您准备的活动优惠，咨询客服了解更多~\\\",\\\"activityUrl\\\":\\\"\\\",\\\"activityEndTime\\\":\\\"\\\",\\\"simpleAnswer\\\":\\\"为您准备的活动优惠，咨询客服了解更多~\\\",\\\"activityBeginTime\\\":\\\"\\\",\\\"activityName\\\":\\\"\\\",\\\"order\\\":2},{\\\"htmlAnswer\\\":\\\"为您准备的活动优惠，咨询客服了解更多~\\\",\\\"activityUrl\\\":\\\"\\\",\\\"activityEndTime\\\":\\\"\\\",\\\"simpleAnswer\\\":\\\"为您准备的活动优惠，咨询客服了解更多~\\\",\\\"activityBeginTime\\\":\\\"\\\",\\\"imageUrl\\\":\\\"http://storage.360buyimg.com/smart-open/upload/80564544230249310.jpg?Expires=3737099616&AccessKey=uqmkb988E8U78xO5&Signature=LnEw1Uybmhd34IFyrUbXXWi7Y8w%3D\\\",\\\"activityName\\\":\\\"\\\",\\\"order\\\":3},{\\\"htmlAnswer\\\":\\\"为您准备的活动优惠，咨询客服了解更多~11#E-s01&lt;strong&gt;111&lt;/strong&gt;\\\",\\\"activityUrl\\\":\\\"https//1111\\\",\\\"activityEndTime\\\":\\\"2020-06-10 15:59\\\",\\\"simpleAnswer\\\":\\\"为您准备的活动优惠，咨询客服了解更多~11#E-s01111\\\",\\\"activityBeginTime\\\":\\\"2020-05-29 15:59\\\",\\\"activityName\\\":\\\"22222\\\",\\\"order\\\":3}]\",\"days\":\"3\"}";
        JSONObject obj1 = JSON.parseObject(str);
        JSONArray array1 = obj1.getJSONArray("activity");
        for(int i = 0; i < array1.size(); i++){
            JSONObject  obj2 = array1.getJSONObject(i);
            System.out.println(obj2.getString("htmlAnswer"));
        }
    }
}
