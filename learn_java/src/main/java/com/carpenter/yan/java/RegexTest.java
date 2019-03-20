package com.carpenter.yan.java;

public class RegexTest {
    private static final String REGEX_NUM = "^\\d+$";

    public static void main(String[] args) {
        testRegex();
    }

    public static void testRegex(){
        System.out.println("12b".matches(REGEX_NUM));
    }
}
