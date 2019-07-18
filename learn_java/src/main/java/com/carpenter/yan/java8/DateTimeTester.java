package com.carpenter.yan.java8;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeTester {
    public static void main(String[] args) {
        testZoneId();
    }

    public static void testLocalDate(){
        LocalDate now = LocalDate.now();
        System.out.println(now);

        now = LocalDate.of(2019, 10, 1);
        System.out.println(now);

        now.plus(1, ChronoUnit.WEEKS);
        System.out.println(now);

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
}
