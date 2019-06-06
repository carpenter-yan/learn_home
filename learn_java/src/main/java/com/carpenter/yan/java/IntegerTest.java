package com.carpenter.yan.java;

public class IntegerTest {
    public static void main(String[] args) {
        testAdd();
    }

    public static void testBoundary(){
        int a = 200 * 300 * 400 * 500;
        System.out.println(a);
    }

    public static void testAdd(){
        Integer a = 5;
        Integer b = 5;
        System.out.print(a+b);
    }

    public static void testCompare(){
        Integer a = 5;
        a.compareTo(5);
    }
}
