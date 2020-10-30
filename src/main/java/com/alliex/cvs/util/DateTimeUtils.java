package com.alliex.cvs.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class DateTimeUtils {

    public static LocalDateTime getMidnight(LocalDateTime dateTime) {
        return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(0, 0, 0));
    }

    public static LocalDateTime getFirstDayOfMonth(LocalDateTime dateTime) {
        LocalDateTime setMidnight = getMidnight(dateTime);
        return setMidnight.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDateTime getFirstDayOfPrevMonth(LocalDateTime dateTime) {
        LocalDateTime setMidnight = getMidnight(dateTime);
        setMidnight = setMidnight.with(TemporalAdjusters.firstDayOfMonth());
        return setMidnight.minusMonths(1L);
    }

    public static LocalDateTime stringToLocalDateTime(String date) {
        return LocalDateTime.of(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd")), LocalTime.of(0, 0, 0));
    }

}