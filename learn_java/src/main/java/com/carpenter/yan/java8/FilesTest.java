package com.carpenter.yan.java8;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FilesTest {

    @Test
    public void test1(){
        String p = "/export/Logs/xz-jimi3-ire.jd.local/orders.csv";
        p = "D:\\export\\Logs\\xz-jimi3-ire.jd.local\\orders.csv";
        Path path= Paths.get(p);

        if(Files.notExists(path)){
            try {
                Files.createFile(path);
                System.out.println(Files.exists(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            bw.append("it's a order\n");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void chgAddCart(){
        Path inputPath = Paths.get("D:\\input.txt");
        if(Files.notExists(inputPath)){
            System.out.println("输入文件不存在");
            return;
        }


        Path outPath = Paths.get("D:\\output.csv");
        if(Files.notExists(outPath)){
            try {
                Files.createFile(outPath);
            } catch (IOException e) {
                System.out.println("创建输出文件失败");
            }
        }

        try {
            BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8);
            BufferedWriter writer = Files.newBufferedWriter(outPath, StandardOpenOption.APPEND);
            reader.lines().forEach((line)->{
                StringBuilder sb = new StringBuilder();
                sb.append("\"{")
                        .append(line.substring(line.indexOf("msg") - 1, line.indexOf("mqType") + 10).replaceAll("\"", "\"\""))
                        .append("}\"\r\n");
                try {
                    writer.write(sb.toString());
                    writer.flush();
                } catch (IOException e) {
                    System.out.println("写文件失败");
                }
            });
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test22(){
        String ab = "ab\"";
        System.out.println(ab.replaceAll("\\\"", "\\\\\""));
    }
}
