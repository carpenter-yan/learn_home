package com.carpenter.yan.jd;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CallBackTest {
    public static final String account = "xiaozhi-open";
    public static final String password = "nKd3fg!ZS7";

    @Test
    public void test1(){
        Map<String, String> result = callBack(null, null, null, null, null);
        Assert.assertEquals("222222", result.get("clcode"));
    }

    @Test
    public void test2(){
        Map<String, String> result = callBack(null, "null", "null", "null", "null");
        Assert.assertEquals("222222", result.get("clcode"));
    }

    @Test
    public void test3(){
        Map<String, String> result = callBack("acc", "pwd", "null", "null", "null");
        Assert.assertEquals("111111", result.get("clcode"));
    }

    @Test
    public void test4(){
        Map<String, String> result = callBack("xiaozhi-open", "pwd", "null", "null", "null");
        Assert.assertEquals("111111", result.get("clcode"));
    }

    @Test
    public void test5(){
        Map<String, String> result = callBack("xiaozhi-open", "nKd3fg!ZS7", "null", "null", "DELIVRD");
        Assert.assertEquals("000000", result.get("clcode"));
    }

    @Test
    public void test6(){
        Map<String, String> result = callBack("xiaozhi-open", "nKd3fg!ZS7", "null", "null", "MALBACK");
        Assert.assertEquals("333333", result.get("clcode"));
    }


    public static Map<String, String> callBack(String receiver,
                                        String pswd,
                                        String msgid,
                                        String mobile,
                                        String status) {
        Map<String, String> result = new HashMap<>();
        result.put("clcode", "000000");
        if (StringUtils.isBlank(receiver)
                || StringUtils.isBlank(pswd)
                || StringUtils.isBlank(msgid)
                || StringUtils.isBlank(mobile)
                || StringUtils.isBlank(status)) {
            result.put("clcode", "222222");
            return result;
        }

        if (!account.equals(receiver) || !password.equals(pswd)) {
            System.out.println("回执接口账号密码错误");
            result.put("clcode", "111111");
            return result;
        }

        if ("DELIVRD".equals(status)) {
            System.out.println("创蓝短信发送成功");
            return result;
        }
        result.put("clcode", "333333");
        return result;
    }
}
