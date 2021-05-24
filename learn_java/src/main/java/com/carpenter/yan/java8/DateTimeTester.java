package com.carpenter.yan.java8;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeTester {

    @Test
    public void testLocalDate() {
        LocalDate now = LocalDate.now();
        System.out.println(now);

        now = LocalDate.of(2019, 10, 1);
        System.out.println(now);

        LocalDate next = now.plus(1, ChronoUnit.WEEKS);
        System.out.println(next);

        System.out.printf("%d:%d:%d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
    }

    @Test
    public void testZoneId() {
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

    @Test
    public void testParse() {
        LocalDate date = LocalDate.parse("2019-07-18");
        System.out.println(date);
        LocalTime time = LocalTime.parse("12:45:20");
        System.out.println(time);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        System.out.println(dateTime);
    }

    @Test
    public void testOfDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2019, 7, 15, 12, 0, 5);
        System.out.println(dateTime);

    }

    @Test
    public void testInstant() {
        Instant instant = Instant.now();
        System.out.println(instant);
    }

    @Test
    public void testCurrentString() {
        System.out.println(LocalTime.now().toString());
    }

    @Test
    public void testFormat() {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testFormat2() {
        System.out.println(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy_MM_dd")));
    }

    @Test
    public void testDate() {
        System.out.println(LocalDate.now().toString());
    }

    @Test
    public void testMutiDate() {
        LocalDate localDate = LocalDate.now();
        for (int i = 0; i < 5; i++) {
            System.out.println(localDate.minusDays(i).toString());
        }
    }

    @Test
    public void testLocalDate2() {
        String abc = LocalDate.now().minusDays(2).format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        String def = String.format("%s:op:bloom:filter:zone%s", abc, 1);
        System.out.println(def);
    }


}
