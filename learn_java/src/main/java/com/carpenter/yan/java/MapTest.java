package com.carpenter.yan.java;

import java.util.*;

public class MapTest {
    public static void main(String[] args) {
        testContain();
    }

    public static void testValuseMethod(){
        Map<Integer, String> englishNumber = new HashMap<>();
        englishNumber.put(1, "one");

        List<String> english = new ArrayList<>(englishNumber.values());

        System.out.println(english.get(0));

        for(Map.Entry<Integer, String> entry : englishNumber.entrySet()){
            entry.getKey();
        }
    }

    public static void testContain() {
        Map<String, String> map = new HashMap<>();
        System.out.println(map.containsKey(null));
        System.out.println(map.containsValue(null));

    }

    public static void testTableSizeFor(){
        int cap = 10;
        Map<String, String> Map = new HashMap<>(10, 0.75f);
    }
}
