package com.carpenter.yan.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListTest {
    public static void main(String[] args) {
        List<String> del = Arrays.asList("1", "2");
        List<String> add = Arrays.asList("2", "3");

        List<String> finalDel = new ArrayList<String>();
        List<String> finalAdd = new ArrayList<String>();
        finalDel.addAll(del);
        finalDel.removeAll(add);
        finalAdd.addAll(add);
        finalAdd.removeAll(del);

        System.out.println(finalAdd.toString());
        System.out.println(finalDel.toString());
    }
}
