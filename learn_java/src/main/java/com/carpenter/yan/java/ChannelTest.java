package com.carpenter.yan.java;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static java.lang.System.out;

public class ChannelTest {
    @Test
    public void test1(){
        File f = new File("D:\\IdeaProjects\\learn_home\\learn_java\\src\\main\\java\\com\\carpenter\\yan\\java\\Apple.java");
        try(
            FileChannel inChannel = new FileInputStream(f).getChannel();
        ) {
            MappedByteBuffer buff = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, f.length());
            buff.flip();
            out.println(buff);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
