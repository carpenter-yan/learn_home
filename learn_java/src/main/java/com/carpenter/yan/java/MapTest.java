package com.carpenter.yan.java;

import java.util.*;

public class MapTest {
    public static void main(String[] args) {
        testValuseMethod();
    }

    public static void testValuseMethod(){
        Map<Integer, String> englishNumber = new HashMap<>();
        englishNumber.put(1, "one");

        List<String> english = new ArrayList<>(englishNumber.values());

        System.out.println(english.get(0));
    }
}
