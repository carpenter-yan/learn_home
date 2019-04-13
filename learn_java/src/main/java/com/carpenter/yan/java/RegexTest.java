package com.carpenter.yan.java;

public class RegexTest {
    private static final String REGEX_NUM = "^\\d+$";
    private static final String REGEX_ORDER_ID = "(^|(?<=[^\\.\\d\\w]))[\\d]{11}($|(?=[^\\d\\w%]))";

    public static void main(String[] args) {
        testRegexOrderId();
    }

    public static void testRegexNum(){
        System.out.println("12b".matches(REGEX_NUM));
    }

    public static void testRegexOrderId(){
        System.out.println("85145831701".matches(REGEX_NUM));
    }
}
