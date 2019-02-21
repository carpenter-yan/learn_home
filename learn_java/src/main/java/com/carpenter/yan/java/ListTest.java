package com.carpenter.yan.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListTest {
    public static void main(String[] args) {
        testBasic();
    }

    public static void testSetOperation(){
        List<String> del = Arrays.asList("1", "2");
        List<String> add = Arrays.asList("2", "3");

        List<String> finalDel = new ArrayList<>();
        List<String> finalAdd = new ArrayList<>();
        finalDel.addAll(del);
        finalDel.removeAll(add);
        finalAdd.addAll(add);
        finalAdd.removeAll(del);

        System.out.println(finalAdd.toString());
        System.out.println(finalDel.toString());
    }

    public static void testBasic(){
        List<String> oneArray = new ArrayList<>();
        oneArray.add("1");
        oneArray.remove("1");
        oneArray.addAll(Arrays.asList("1", "2"));
        oneArray.get(1);
        System.out.println(oneArray.removeAll(Arrays.asList("3")));
    }
}
