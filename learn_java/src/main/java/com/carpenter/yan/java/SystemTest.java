package com.carpenter.yan.java;

import static java.lang.System.out;

public class SystemTest {
    public static void main(String[] args) {
        testJavaProp();
    }
    public static void testCurrentTimeMill(){
        out.println(System.currentTimeMillis());
        out.println(System.currentTimeMillis()-60*1000);
        out.println(System.currentTimeMillis()-24*60*60*1000);
        out.println(System.currentTimeMillis()+24*60*60*1000);
    }

    public static void testJavaProp(){
        out.println(System.getProperty("sun.boot.class.path"));
        out.println(System.getProperty("java.ext.dirs"));
        out.println(System.getProperty("java.class.path"));
    }
}
