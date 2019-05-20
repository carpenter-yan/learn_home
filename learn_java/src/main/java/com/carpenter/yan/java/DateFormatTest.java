package com.carpenter.yan.java;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        Date date = new Date(1557970068715L);
        System.out.println(date.toString());
    }
}
