package base.java;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;
import static java.lang.System.out;

public class SystemTest {
    public static void main(String[] args) throws IOException {
        BufferedReader is = new BufferedReader(new InputStreamReader(in));
        String input = is.readLine();
        String[] array = input.split(" ");
        if(array.length == 2){
            int low = Integer.valueOf(array[0]);
            int high = Integer.valueOf(array[1]);
            List<String> result = new ArrayList<>();
            for(int i = low; i <= high; i++){
                int unit = i % 10;
                int ten = i / 10 % 10;
                int hundread = i / 100 % 10;
                if(Math.pow(hundread, 3) + Math.pow(ten, 3) + Math.pow(unit, 3) == i){
                    result.add(String.valueOf(i));
                }
            }
            if(result.size() > 0){
                System.out.println(String.join(" ", result));
            }
        }
    }

    @Test
    public void testCurrentTimeMill(){
        out.println(System.currentTimeMillis());
        out.println(System.currentTimeMillis()-60*1000);
        out.println(System.currentTimeMillis()-24*60*60*1000);
        out.println(System.currentTimeMillis()+24*60*60*1000);
    }

    @Test
    public void testJavaProp(){
        out.println(System.getProperty("sun.boot.class.path"));
        out.println(System.getProperty("java.ext.dirs"));
        out.println(System.getProperty("java.class.path"));
    }

    @Test
    public void testOS(){
        System.out.println(System.getProperty("os.name").toLowerCase());
    }
}
