package base;

import org.junit.Test;

public class MethodTest {

    public String[] getKeywords() {
        return new String[]{"iphone"};
    }

    @Test
    public void test1(){
        System.out.println(getKeywords()[0]);
    }
}
