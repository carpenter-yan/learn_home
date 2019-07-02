package com.carpenter.yan.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Convert {
    public static void main(String[] args) {
        Student st = new Student();
        st.setName("");
        st.setName(null);
        System.out.println(JSON.toJSONString(st));
    }

    public static void test1(){
        Student st = new Student();
        st.setId(1);
        st.setName("123");
        JSONObject jo = JSON.parseObject(JSON.toJSONString(st));
        jo.put("age", 12);
        System.out.println(jo.toJSONString());
    }
}
