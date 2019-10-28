package com.carpenter.yan.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Convert {
    public static void main(String[] args) {
        test4();
    }

    public static void test1(){
        Student st = new Student();
        st.setId(1);
        st.setName("123");
        JSONObject jo = JSON.parseObject(JSON.toJSONString(st));
        jo.put("age", 12);
        System.out.println(jo.toJSONString());
    }

    public static void test2(){
        JSONObject site = new JSONObject();
        site.put("buid", 301);
        site.put("clientCode", "CN");
        site.put("lang", "zh");

        JSONObject param = new JSONObject();
        param.put("aggr", 1);
        param.put("fuzzy", 0);
        param.put("objectIds", "271235,156774");
        param.put("clientType", "zhineng");
        param.put("site", site);
        System.out.println(param.toJSONString());
    }

    public static void test3(){
        String c = "{\"data\":[{\"content\":\"<p>亲爱的~双11预售活动已经开始,保价双11,放心提前购,记得先支付定金,抢的好礼哦#E-s21\n" +
                "<a href=\\\"https://pro.jd.com/mall/active/2bPfaoa7brrm1tBKtTu7y433ggMF/index.html\\\" target=\\\"_self\\\" class=\\\"J_Link\\\" style=\\\"color: rgb(255, 0, 0); text-decoration: underline;\\\"><span style=\\\"color: rgb(255, 0, 0);\\\"><strong>双11预售分会场</strong></span></a>\n" +
                "</p>\",\"type\":\"1\"},{\"content\":\"http://storage.360buyimg.com/smart-open/upload/44306317285981921.png?Expires=3719289438&AccessKey=uqmkb988E8U78xO5&Signature=SNOVkRmajap%2BiLLoz%2B3dVxPCwKY%3D\",\"type\":\"2\"}]}";


        String d = "{\\\"data\\\":[{\\\"content\\\":\\\"<p>亲爱的~双11预售活动已经开始,保价双11,放心提前购,记得先支付定金,抢的好礼哦#E-s21\n" +
                "<a href=\\\\\"https://pro.jd.com/mall/active/2bPfaoa7brrm1tBKtTu7y433ggMF/index.html\\\\\" target=\\\\\"_self\\\\\" class=\\\\\"J_Link\\\\\" style=\\\\\"color: rgb(255, 0, 0); text-decoration: underline;\\\\\"><span style=\\\\\"color: rgb(255, 0, 0);\\\\\"><strong>双11预售分会场</strong></span></a>\n" +
                "</p>\\\",\\\"type\\\":\\\"1\\\"},{\\\"content\\\":\\\"http://storage.360buyimg.com/smart-open/upload/44306317285981921.png?Expires=3719289438&AccessKey=uqmkb988E8U78xO5&Signature=SNOVkRmajap%2BiLLoz%2B3dVxPCwKY%3D\\\",\\\"type\\\":\\\"2\\\"}]}";

        String e = "{ \"message\":\"{\"data\":[{\"content\":\"<span style=\\\"color: rgb(227, 108, 9);\\\">【您好】</span>，您订购的预售商品尾款支付时间快要截止啦，建议您尽快支付哦 #E-s01\",\"type\":\"1\"},{\"content\":\"http://storage.360buyimg.com/smart-open/upload/104875834613947467.jpg?Expires=3719458292&AccessKey=uqmkb988E8U78xO5&Signature=mfpzoIIQ%2F2Et9nDJDeOYXuroX7o%3D\",\"type\":\"2\"}]}\",\"btns\": [{ \"isShowBtn\": true, \"btnType\": 1, \"btnText\": \"去付款\", \"url\": \"openApp.jdMobile://virtual?params={\\\"category\\\":\\\"jump\\\",\\\"des\\\":\\\"orderlist\\\",\\\"functionId\\\":\\\"daifukuan\\\"}\", \"cbData\":{ \"default\":{ } } }] }";

        Object objc = JSON.parse(c);
        System.out.println("parse c ok");

        Object objd = JSON.parse(e);
        System.out.println("parse d ok");

   }

   public static void test4(){
        String a = "{ \"message\":\"<p>用户下单未付款--答案【卖场型老开新-717748】<a href=\"https://www.jd.com\" target=\"_self\" class=\"J_Link\">www</a>#E-s01</p>\",\"btns\": [{ \"isShowBtn\": true, \"btnType\": 1, \"btnText\": \"去付款\", \"url\": \"https://order.jd.com/center/list.action?s=1&utm_source=kong&utm_medium=cpc&utm_campaign=t_1001464481_xz\", \"cbData\":{\"default\":{}}}]}";
        //String b = a.replaceAll("\\\"","\\\\\"");
        Object objb = JSON.toJSON(a);
   }
}
