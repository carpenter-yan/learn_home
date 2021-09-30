package com.carpenter.yan.java;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class SetTest {
    @Test
    public void test11(){
        Integer a = new Integer(4);
        Integer b = new Integer(1000);
        Set<Integer> set = new HashSet<>();
        set.add(a);
        set.add(b);
        Assert.assertTrue(set.contains(4));
        Assert.assertTrue(set.contains(1000));
    }
}
