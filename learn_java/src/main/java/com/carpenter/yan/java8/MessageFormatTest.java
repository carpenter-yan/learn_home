package com.carpenter.yan.java8;

import org.junit.Test;

import java.text.MessageFormat;

public class MessageFormatTest {
    @Test
    public void test1(){
        System.out.println(MessageFormat.format("addcart.pin.limit.prefix.{0}", "dasuantou"));
    }
}
