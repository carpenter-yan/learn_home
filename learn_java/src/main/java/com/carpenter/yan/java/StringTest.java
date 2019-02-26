package com.carpenter.yan.java;

public class StringTest {
    public static void main(String[] args) {
        testSplit();
    }

    public static void testSplit() {
        String src = "a@&b";
        String[] dest = src.split("@&");
        for(String each : dest){
            System.out.println(each);
        }
    }
}
