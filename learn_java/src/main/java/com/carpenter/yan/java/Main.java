package com.carpenter.yan.java;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

public class Main {
    public static void main(String[] args){
        int i = 1;
        System.out.println((++i)*5);
    }

    @Test
    public void test1(){
        int i = 0;
        i = i > 0 ? echo(--i) : echo(++i);
        System.out.println(i);
    }

    public int echo(int i){
        System.out.println(i);
        return 2;
    }

    @Test
    public void testContain(){
        Integer a[] = {10000, 20000, 30000};
        Long b = Long.valueOf(10000);
        System.out.println(ArrayUtils.contains(a, b));
    }
}
