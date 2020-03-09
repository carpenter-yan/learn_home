package com.carpenter.yan.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

public class FastJsonDemo {

    @Test
    public void test1(){
        String origin = "{\"messageTime\":\"1582093824019\",\"sendTo\":\"{\\\"app\\\":\\\"jd.waiter\\\",\\\"clientType\\\":\\\"jimi\\\",\\\"pin\\\":\\\"open_dev_30\\\"}\",\"address\":\"\",\"wareId\":\"2983863\",\"orderId\":\"\",\"sendFrom\":\"{\\\"app\\\":\\\"im.customer\\\",\\\"clientType\\\":\\\"android\\\",\\\"pin\\\":\\\"open_dev_30\\\"}\",\"finalModelCode\":\"xiaozhi-chufangdianqi3\",\"venderId\":\"747789\",\"messageId\":\"52cafceda8e84c96b580a7c545e2c636\",\"finalCateName\":\"烘焙烘烤\",\"sessionId\":\"jimiLT_1_d461c70da4cb4f5196da774c5886aa05\",\"reqFrom\":\"\",\"clientIP\":\"11.18.159.192\",\"botId\":\"80\"}";
        JSONObject jo = JSON.parseObject(origin);
        System.out.println(jo.getString("sendFrom"));
        JSONObject sendFrom = jo.getJSONObject("sendFrom");
        System.out.println(sendFrom.getString("pin"));
    }

    @Test
    public void test2(){
        Long a = -1L;
        System.out.println(a == -1L);
    }
}
