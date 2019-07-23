package com.carpenter.yan.java;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatTest {
    public static void main(String[] args) {
        testUnixTimeToDate();
    }
    public static void testSimpleDateFormat(){
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

    public static void testUnixTimeToDate(){
        Date date1 = new Date(1563875828000L);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(date1));

        Date date2 = new Date(1564136828000L);
        System.out.println(sdf.format(date2));
    }

    public static void testDateOperation(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(cal.getTime()));

        cal.add(Calendar.DAY_OF_YEAR, 3);
        System.out.println(sdf.format(cal.getTime()));

    }
}
