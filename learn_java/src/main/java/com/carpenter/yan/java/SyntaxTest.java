package com.carpenter.yan.java;

public class SyntaxTest {
    public static void main(String[] args) {
        testTryFinally();
    }
    public static void testTryFinally(){
        try {
            System.out.println("1");
            Long.parseLong("");
            System.out.println("2");
            return;
        }finally {
            System.out.println("3");
        }
    }
}
