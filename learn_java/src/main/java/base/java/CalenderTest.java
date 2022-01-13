package base.java;

import org.junit.Test;

import java.util.Calendar;

public class CalenderTest {
    @Test
    public void test1(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, - 90);
        System.out.println(calendar.getTime());
    }
}
