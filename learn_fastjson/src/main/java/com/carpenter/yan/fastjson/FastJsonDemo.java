package com.carpenter.yan.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FastJsonDemo {

    @Test
    public void test1() {
        String origin = "{\"messageTime\":\"1582093824019\",\"sendTo\":\"{\\\"app\\\":\\\"jd.waiter\\\",\\\"clientType\\\":\\\"jimi\\\",\\\"pin\\\":\\\"open_dev_30\\\"}\",\"address\":\"\",\"wareId\":\"2983863\",\"orderId\":\"\",\"sendFrom\":\"{\\\"app\\\":\\\"im.customer\\\",\\\"clientType\\\":\\\"android\\\",\\\"pin\\\":\\\"open_dev_30\\\"}\",\"finalModelCode\":\"xiaozhi-chufangdianqi3\",\"venderId\":\"747789\",\"messageId\":\"52cafceda8e84c96b580a7c545e2c636\",\"finalCateName\":\"烘焙烘烤\",\"sessionId\":\"jimiLT_1_d461c70da4cb4f5196da774c5886aa05\",\"reqFrom\":\"\",\"clientIP\":\"11.18.159.192\",\"botId\":\"80\"}";
        JSONObject jo = JSON.parseObject(origin);
        System.out.println(jo.getString("sendFrom"));
        JSONObject sendFrom = jo.getJSONObject("sendFrom");
        System.out.println(sendFrom.getString("pin"));
    }

    @Test
    public void test2() {
        Long a = -1L;
        System.out.println(a == -1L);
    }


    @Test
    public void test3() {
        String value = "{\"cList\":[{\"cId\":\"737_794_798\",\"cNm\":\"平板电视\",\"fId\":737,\"currentId\":798},{\"cId\":\"737_13297\",\"cNm\":\"厨卫大电\",\"fId\":737,\"currentId\":13297}],\"startHour\":\"00\",\"endHour\":\"17\"}";
        JSONArray categories = JSON.parseObject(value).getJSONArray("cList");

        List<String> cate1 = new ArrayList<>();
        List<String> cate2 = new ArrayList<>();
        List<String> cate3 = new ArrayList<>();
        categories.forEach(category -> {
            String cIdStr = ((JSONObject) category).getString("cId");
            System.out.println("cid=" + cIdStr);
            String[] cIdArray = cIdStr.split("_");
            if (cIdArray.length > 2) {
                cate3.add(cIdArray[2]);
                return;
            }

            if (cIdArray.length > 1) {
                cate2.add(cIdArray[1]);
                return;
            }

            if (cIdArray.length > 0) {
                cate1.add(cIdArray[0]);
            }
        });
        System.out.println(cate1);
        System.out.println(cate2);
        System.out.println(cate3);
    }

}
