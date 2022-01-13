package base.java;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

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

    @Test
    public void test1(){
        File f = new File("file.txt");
        out.println(f.getName());
        out.println(f.getParent());
        out.println(f.getAbsolutePath());
        out.println(f.getAbsoluteFile());
        out.println(f.getAbsoluteFile().getParent());
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println(f.exists());
        out.println(f.canWrite());
        out.println(f.isFile());
        out.println(f.lastModified());
    }

    @Test
    public void testListRoots(){
        File[] roots = File.listRoots();
        for(File f : roots){
            out.println(f.toString());
        }
    }
}
