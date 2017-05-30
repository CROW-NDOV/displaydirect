package nl.crowndov.displaydirect.distribution.util;

import nl.crowndov.displaydirect.distribution.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class DateTimeUtil {

    public static ZonedDateTime getTime(LocalDate date, String time) {
        String[] split = time.split(":");
        int hour = Integer.valueOf(split[0]);
        boolean addDay = false;
        if (hour > 23) {
            hour = hour - 24;
            addDay = true;
        }
        ZonedDateTime dateTime = ZonedDateTime.of(date, LocalTime.of(hour, Integer.valueOf(split[1]), Integer.valueOf(split[2])), Configuration.getZoneId());
        if (addDay) {
            dateTime = dateTime.plusDays(1);
        }
        return dateTime;
    }

    public static long getMinutesTill(String time) {
        String[] split = time.split(":");
        return getMinutesTill(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
    }

    public static long getMinutesTill(int hour) {
        return getMinutesTill(hour, 0);
    }

    public static long getMinutesTill(int hour, int min) {
        ZonedDateTime tomorrow = ZonedDateTime.now(Configuration.getZoneId()).plusDays(1).withHour(hour).withMinute(min);
        return ChronoUnit.MINUTES.between(ZonedDateTime.now(Configuration.getZoneId()), tomorrow);
    }
}
