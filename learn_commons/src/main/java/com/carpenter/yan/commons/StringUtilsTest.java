package com.carpenter.yan.commons;

import org.apache.commons.lang3.StringUtils;

public class StringUtilsTest {
    public static void main(String[] args) {
        testIsNumber();
    }
    public static void testIsNumber(){
        System.out.println(StringUtils.isAlphanumeric("1234_b"));
    }

    public static void testRemoveStart(){
        String var = "JIMI_3276670118A34EBC96736805184734C6";
        if(StringUtils.startsWith(var, "JIMI_")){
            var = StringUtils.removeStart(var, "JIMI_");
        }
        System.out.println(var);
    }

}
