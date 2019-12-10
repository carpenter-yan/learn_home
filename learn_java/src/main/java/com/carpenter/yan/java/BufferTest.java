package com.carpenter.yan.java;

import org.junit.Test;

import java.nio.CharBuffer;

import static java.lang.System.out;

public class BufferTest {
    @Test
    public void testBasic(){
        CharBuffer buff = CharBuffer.allocate(8);
        out.println("capacity:" + buff.capacity());
        out.println("limit:" + buff.limit());
        out.println("position:" + buff.position());

        buff.put('a').put('b').put('c');
        out.println("position:" + buff.position());

        buff.flip();
        out.println("get:" + buff.get());
        out.println("position:" + buff.position());

        buff.clear();
        out.println("limit:" + buff.limit());
        out.println("position:" + buff.position());

        out.println("get:" + buff.get(0));
        out.println("position:" + buff.position());

    }
}
