package com.carpenter.yan.java;

public class SystemTest {
    public static void main(String[] args) {
        testCurrentTimeMill();
    }
    public static void testCurrentTimeMill(){
        System.out.println(System.currentTimeMillis()*2);
        System.out.println(System.currentTimeMillis()/1000);
    }
}
