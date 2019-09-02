package com.carpenter.yan.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class StreamTest {

    @Test
    public void testInStream(){
        IntStream evenNumbers = IntStream.rangeClosed(1, 100).filter(a->a%2==0);
        System.out.println(evenNumbers.count());
    }

    @Test
    public void testArrayStream(){
        int[] numbers = {2,3,5,7,11,13};
        int sum = Arrays.stream(numbers).sum();
        System.out.println(sum);
    }

    @Test
    public void testIterate(){
        Stream.iterate(0, n->n+2).limit(10).forEach(System.out::println);
    }

    public void testGroupingBy(){
        List<String> inv = new ArrayList<>();
        inv.sort(comparing(String::length));
    }
}
