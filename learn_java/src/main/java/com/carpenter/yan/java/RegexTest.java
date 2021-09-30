package com.carpenter.yan.java;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    private static final String REGEX_NUM = "^\\d+$";
    private static final String REGEX_ORDER_ID = "(^|(?<=[^\\.\\d\\w]))[\\d]{11}($|(?=[^\\d\\w%]))";

    public static void main(String[] args) {
        testRegexOrderId();
    }

    public static void testRegexNum() {
        System.out.println("12b".matches(REGEX_NUM));
    }

    public static void testRegexOrderId() {
        System.out.println("85145831701".matches(REGEX_NUM));
    }

    @Test
    public void test1() {
        String pattern = ".*runoob.*";
        String content = "I am noob from runoob.com";
        System.out.println(Pattern.matches(pattern, content));
    }

    @Test
    public void test2() {
        String pattern = "^able";
        String content1 = "able";
        String content2 = "be able";
        Pattern p = Pattern.compile(pattern);
        System.out.println(p.matcher(content1).matches());
        System.out.println(p.matcher(content2).matches());
    }

    @Test
    public void test3() {
        Pattern p = Pattern.compile("^\\d{11}");
        Matcher m = p.matcher("13812345678");
        System.out.println(m.matches());
    }

    @Test
    public void test4() {
        Matcher m = Pattern.compile("\\w+").matcher("Java is very easy");
        while (m.find()) {
            System.out.println(m.group() + " begin at:" + m.start() + " end at:" + m.end());
        }
    }

    @Test
    public void test5() {
        String[] msgs = {"Java has regular expression in 1.4",
                "regular expression now expressing in Java",
                "Java represses oracular expressions"};
        Pattern p = Pattern.compile("re\\w*");
        Matcher m = null;
        for (int i = 0; i < msgs.length; i++) {
            if (m == null) {
                m = p.matcher(msgs[i]);
            } else {
                m.reset(msgs[i]);
            }
            System.out.println(m.replaceAll("哈哈:)"));
        }
    }

    @Test
    public void test6(){
        String regex = "(^\\d{2})|(\\d{2}$)";
        String source = "11201234213342";
        String result = Pattern.compile(regex).matcher(source).replaceAll("**");
        System.out.println(result);
    }
}
