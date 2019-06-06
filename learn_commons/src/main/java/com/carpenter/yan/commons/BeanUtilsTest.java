package com.carpenter.yan.commons;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class BeanUtilsTest {
    public static void main(String[] args) {
        testClone();
    }

    public static void testClone(){
        try {
            Map<String, String> src = new HashMap<>();
            src.put("one", "1");
            Map<String, String> dst = new HashMap<>();
            BeanUtils.copyProperties(dst, src);
            dst.put("one", "2");
            System.out.println(src.get("one"));
            System.out.println(dst.get("one"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
