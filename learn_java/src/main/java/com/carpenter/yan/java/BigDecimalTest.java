package com.carpenter.yan.java;

import java.math.BigDecimal;

public class BigDecimalTest {
    public static void main(String[] args) {
        testOpr();
    }

    public  static void testNew(){
        double d1 = new BigDecimal("0.012").doubleValue();
        double d2 = BigDecimal.valueOf(0.012).doubleValue();
        double d3 = new BigDecimal(0.012).doubleValue();
        System.out.println(d1 == d2);
        System.out.println(d1 == d3);
        System.out.println(d2 == d3);
    }

    public static void testOpr(){
        BigDecimal b1 = BigDecimal.valueOf(0.05);
        BigDecimal b2 = BigDecimal.valueOf(0.01);
        System.out.println(b1.divide(b2));
    }
}
