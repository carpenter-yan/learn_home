package base.java;

import org.junit.Test;

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

    @Test
    public void test1(){
        Integer a = Integer.valueOf(1);
        Integer b = Integer.valueOf(2);
        Integer c = a + b;
        System.out.println(c);
    }

    @Test
    public void testValueOf(){
        System.out.println(Integer.valueOf("01"));
    }
}
