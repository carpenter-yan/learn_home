package base.java;

import org.junit.Test;

public class ExceptionTest {

    @Test
    public void testGetMsg(){
        Exception e = new NullPointerException();
        System.out.println(e.getClass().getSimpleName());
    }
}
