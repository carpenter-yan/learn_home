package com.carpenter.yan.commons;

import org.apache.commons.lang3.RandomUtils;

public class RandomUtilsTest {
    public static void main(String[] args) {
        testRandom();
    }

    public static void testRandom(){
        for(int i = 0; i < 100; i++){
            System.out.println(RandomUtils.nextInt(0, 100));
        }
    }
}
