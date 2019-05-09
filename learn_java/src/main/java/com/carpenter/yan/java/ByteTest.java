package com.carpenter.yan.java;

public class ByteTest {
    public static void main(String[] args) {
        testBytes();
    }
    public static void testBytes(){
        String abc = "d1360";
        byte[] array = abc.getBytes();
        for(byte a: array){
            System.out.print(a);
        }
    }
}
