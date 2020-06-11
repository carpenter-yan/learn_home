package com.carpenter.yan.joda;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class DateTimeTest {
    @Test
    public void test1(){
        DateTime dt = new DateTime("2020-05-16T09:19:00");
        System.out.print(dt.toString());
    }

    @Test
    public void test2(){
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime begin = DateTime.parse("2020-05-15 13:10", df);
        DateTime end = DateTime.parse("2020-06-16 13:00", df);
        if(begin.isBeforeNow() && end.isAfterNow()){
            System.out.print(true);
        }
    }

    @Test
    public void test3(){
        String cfg = "{\"days\":\"3\",\"cron\":\"0 12 ? ? ? ?\"," +
                "\"activity1Name\":\"\",\"activity1Url\":\"\",\"activity1ImageUrl\":\"\",\"activity1BeginTime\":\"\"," +
                "\"activity1EndTime\":\"\",\"activity1SimpleAnswer\":\"\",\"activity1HtmlAnswer\":\"\"," +
                "\"activity2Name\":\"\",\"activity2Url\":\"\",\"activity2ImageUrl\":\"\",\"activity2BeginTime\":\"\"," +
                "\"activity2EndTime\":\"\",\"activity2SimpleAnswer\":\"\",\"activity2HtmlAnswer\":\"\"," +
                "\"activity3Name\":\"\",\"activity3Url\":\"\",\"activity3ImageUrl\":\"\",\"activity3BeginTime\":\"\"," +
                "\"activity3EndTime\":\"\",\"activity3SimpleAnswer\":\"\",\"activity3HtmlAnswer\":\"\"}";
        System.out.println(cfg);
    }
}
