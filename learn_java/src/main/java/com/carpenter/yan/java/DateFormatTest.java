package com.carpenter.yan.java;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateFormatTest {
    @Test
    public void testSimpleDateFormat(){
        DateFormat df1 = DateFormat.getDateTimeInstance();
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println(df1.format(date));
        System.out.println(df2.format(date));
        try {
            System.out.println(df2.parse("2019-05-13 16:36:20").getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testUnixTimeToDate(){
        Date date1 = new Date(1565230671979L);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(date1));

        Date date2 = new Date(1564136828000L);
        System.out.println(sdf.format(date2));
        //2019-08-08 10:17:51
        //2019-08-08 10:17:52.074
    }

    @Test
    public void testDateOperation(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(cal.getTime()));

        cal.add(Calendar.DAY_OF_YEAR, 3);
        System.out.println(sdf.format(cal.getTime()));

    }

    @Test
    public void printUnixTime(){
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void test1(){
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println("current="+System.currentTimeMillis()+"milliSecond=" + milliSecond);
        System.out.println("today_start=" + (todayStart.toInstant(ZoneOffset.of("+8")).toEpochMilli()));
        System.out.println("today_end=" + (todayEnd.toInstant(ZoneOffset.of("+8")).toEpochMilli()));
        System.out.println("yesterday_start=" + (todayStart.toInstant(ZoneOffset.of("+8")).toEpochMilli() - 86400000));
        System.out.println("yesterday_end=" + (todayEnd.toInstant(ZoneOffset.of("+8")).toEpochMilli() - 86400000));
        System.out.println("2day_start=" + (todayStart.toInstant(ZoneOffset.of("+8")).toEpochMilli() - 86400000*2));
        System.out.println("2day_end=" + (todayEnd.toInstant(ZoneOffset.of("+8")).toEpochMilli() - 86400000*2));
        System.out.println("3day_start=" + (todayStart.toInstant(ZoneOffset.of("+8")).toEpochMilli() - 86400000*3));
        System.out.println("3day_end=" + (todayEnd.toInstant(ZoneOffset.of("+8")).toEpochMilli() - 86400000*3));

        long random = System.currentTimeMillis() % 2;
        System.out.println("random=" + random);
        //System.out.println(Double.valueOf(random + random/5));

        LocalDateTime today = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("today=" + df.format(today));
    }

    @Test
    public void test2(){
        Long today = 1571328000000L;
        System.out.println(today - 86400000);
    }

    @Test
    public void test3(){
        System.out.println(new Date(1573920000000L));
        System.out.println(new Date(1574352000000L));//1571455792000
        System.out.println(new Date(1574438399999L));
    }

    @Test
    public void test4(){
        Long begin  = LocalDateTime.of(LocalDate.now(), LocalTime.now()).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Long end = begin - 86400000;
        System.out.println("begin="+ begin + ",end=" + end);
    }
}
