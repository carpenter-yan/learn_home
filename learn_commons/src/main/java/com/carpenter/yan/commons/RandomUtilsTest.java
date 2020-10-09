package com.carpenter.yan.commons;

import org.apache.commons.lang3.RandomUtils;

public class RandomUtilsTest {
    public static void main(String[] args) {
        testRandom2();
    }

    public static void testRandom(){
        for(int i = 0; i < 100; i++){
            System.out.println(RandomUtils.nextInt(0, 100));
        }
    }

    public static void testRandom2() {
        int succ = 0;
        int fail = 0;
        for(int i = 0; i < 100000; i++){
            if(Math.random() * 100 > 50){
                succ++;
            } else {
                fail++;
            }
        }
        System.out.println(succ + ":" + fail);
    }
}
