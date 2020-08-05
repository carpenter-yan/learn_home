package com.carpenter.yan.java8;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeTester {
    public static void main(String[] args) {
        testCurrentString();
    }

    public static void testLocalDate(){
        LocalDate now = LocalDate.now();
        System.out.println(now);

        now = LocalDate.of(2019, 10, 1);
        System.out.println(now);

        LocalDate next = now.plus(1, ChronoUnit.WEEKS);
        System.out.println(next);

        System.out.printf("%d:%d:%d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
    }

    public static void testZoneId() {
        // 上海时间
        ZoneId shanghaiZoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime shanghaiZonedDateTime = ZonedDateTime.now(shanghaiZoneId);

        // 东京时间
        ZoneId tokyoZoneId = ZoneId.of("Asia/Tokyo");
        ZonedDateTime tokyoZonedDateTime = ZonedDateTime.now(tokyoZoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("上海时间: " + shanghaiZonedDateTime.format(formatter));
        System.out.println("东京时间: " + tokyoZonedDateTime.format(formatter));

    }

    public static void testParse(){
        LocalDate date = LocalDate.parse("2019-07-18");
        System.out.println(date);
        LocalTime time = LocalTime.parse("12:45:20");
        System.out.println(time);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        System.out.println(dateTime);
    }

    public static void testOfDateTime(){
        LocalDateTime dateTime = LocalDateTime.of(2019, 7, 15, 12, 0, 5);
        System.out.println(dateTime);

    }

    public static void testInstant(){
        Instant instant = Instant.now();
        System.out.println(instant);
    }

    public static void testCurrentString(){
        System.out.println(LocalTime.now().toString());
    }
}
