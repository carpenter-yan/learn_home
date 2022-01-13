package base.java;

import org.junit.Test;

import java.util.ArrayList;

public class ClassTest {
    @Test
    public void test1(){
        Class cls = ArrayList.class;
        System.out.println(cls.getSimpleName());
    }
}
