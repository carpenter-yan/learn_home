package com.carpenter.yan.java;

import java.io.File;
import static java.lang.System.out;

public class FileTest {
    public static void main(String[] args) {
        testFile();
    }

    public static void testFile(){
        File file = new File("test");
        out.println(file.getName());
        out.println(file.getAbsoluteFile());
        out.println(file.getParent());
        out.println(file.getAbsoluteFile().getParent());
    }
}
