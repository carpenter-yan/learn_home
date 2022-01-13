package base.java8;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.out;

public class FilesTest {
    /**
     * 求两个文件交集
     */
    @Test
    public void inner() {
        String fileAim = "D:\\lookalike\\1012\\vivob8000.txt";
        String fileOrder = "D:\\lookalike\\1012\\vivo1001bbrowse.txt";
        String fileResult = fileAim.replace(".", "_output.");

        //1.检测文件是否存在
        Path pathA = Paths.get(fileAim);
        if (Files.notExists(pathA)) {
            out.println("file a not exist");
            return;
        }

        Path pathB = Paths.get(fileOrder);
        if (Files.notExists(pathB)) {
            out.println("file b not exist");
            return;
        }

        Path pathC = Paths.get(fileResult);
        if (Files.notExists(pathC)) {
            try {
                Files.createFile(pathC);
            } catch (IOException e) {
                System.out.println("create file c error");
            }
        }

        try {
            BufferedReader readerA = Files.newBufferedReader(pathA, StandardCharsets.UTF_8);
            Set<String> setA = new HashSet<>(128);
            readerA.lines().forEach(item -> setA.add(item.split(",")[1]));
            //readerA.lines().forEach(item -> setA.add(item));

            BufferedReader readerB = Files.newBufferedReader(pathB, StandardCharsets.UTF_8);
            Set<String> setB = new HashSet<>(128);
            readerB.lines().forEach(item -> setB.add(item));

            readerA.close();
            readerB.close();

            setA.retainAll(setB);

            out.println("setA.size=" + setA.size());

            if (setA.size() > 0) {
                BufferedWriter writerC = Files.newBufferedWriter(pathC, StandardOpenOption.APPEND);
                setA.stream().forEach(item -> {
                    try {
                        writerC.write(item);
                        writerC.write("\r\n");
                        writerC.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                writerC.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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

    @Test
    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("D:\\sensitive.txt"));
            for (String line : lines) {
                String[] word = line.split("\\t");
                System.out.println(word[0] + ":" + word[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
