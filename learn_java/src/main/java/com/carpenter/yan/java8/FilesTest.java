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
    public void chgOrder() {
        Path inputPath = Paths.get("D:\\orders421b.csv");
        if (Files.notExists(inputPath)) {
            System.out.println("输入文件不存在");
            return;
        }


        Path outPath = Paths.get("D:\\orders10003.csv");
        if (Files.notExists(outPath)) {
            try {
                Files.createFile(outPath);
            } catch (IOException e) {
                System.out.println("创建输出文件失败");
            }
        }

        try {
            BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8);
            BufferedWriter writer = Files.newBufferedWriter(outPath, StandardOpenOption.APPEND);
            reader.lines().forEach((line) -> {
                if (line.length() > 12000) {
                    return;
                }

                if (!line.startsWith("\"<Order")) {
                    return;
                }

                if (line.lastIndexOf("\"<Order") > 10) {
                    return;
                }

                String sb = line.replaceAll("\\s{2,}", "");
                try {
                    writer.write(sb);
                    writer.write("\r\n");
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
    public void chgAddCart() {
        Path inputPath = Paths.get("D:\\input.txt");
        if (Files.notExists(inputPath)) {
            System.out.println("输入文件不存在");
            return;
        }


        Path outPath = Paths.get("D:\\output.csv");
        if (Files.notExists(outPath)) {
            try {
                Files.createFile(outPath);
            } catch (IOException e) {
                System.out.println("创建输出文件失败");
            }
        }

        try {
            BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8);
            BufferedWriter writer = Files.newBufferedWriter(outPath, StandardOpenOption.APPEND);
            reader.lines().forEach((line) -> {
                StringBuilder sb = new StringBuilder();
                sb.append("\"{")
                        .append(line.substring(line.indexOf("\"msg\""), line.indexOf("mqType") + 10).replaceAll("\"", "\"\""))
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
    public void computeOrderLength() {
        Path inputPath = Paths.get("D:\\orders.csv");
        if (Files.notExists(inputPath)) {
            System.out.println("输入文件不存在");
            return;
        }

        try {
            BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8);
            reader.lines().forEach((line) -> {
                System.out.println(line.length());
            });
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
