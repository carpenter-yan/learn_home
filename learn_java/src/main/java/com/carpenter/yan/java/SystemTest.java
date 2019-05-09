package com.carpenter.yan.java;

public class SystemTest {
    public static void main(String[] args) {
        testCurrentTimeMill();
    }
    public static void testCurrentTimeMill(){
        System.out.println(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis()-60*1000);
        System.out.println(System.currentTimeMillis()-24*60*60*1000);
        System.out.println(System.currentTimeMillis()+24*60*60*1000);
    }
}
