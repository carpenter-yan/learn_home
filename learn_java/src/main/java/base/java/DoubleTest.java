package base.java;

import org.junit.Test;

public class DoubleTest {
    public static void main(String[] args) {
        System.out.println(100 < Double.MAX_VALUE);
    }

    @Test
    public void test1() {
        float a = 0.125f;
        double b = 0.125;
        System.out.println(a - b);
    }

    @Test
    public void test2() {
        double a = 0.8;
        double b = 0.7;
        double c = 0.6;
        System.out.println(a - b == b - c);
    }

    @Test
    public void test3() {
        String a = "1.4495259E-6";
        System.out.println(Float.valueOf(a).floatValue() > 0.00000001);
    }
}
