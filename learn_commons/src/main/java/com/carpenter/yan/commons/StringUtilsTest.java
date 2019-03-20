package com.carpenter.yan.commons;

import org.apache.commons.lang3.StringUtils;

public class StringUtilsTest {
    public static void main(String[] args) {
        testIsNumber();
    }
    public static void testIsNumber(){
        System.out.println(StringUtils.isNumeric("123b"));
    }
}
