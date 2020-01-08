package com.project.courierapp.model.calculator;

import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class TimeCalculator {

    private static final int SECCONDS_IN_HOUER = 3600;
    private static final int MINUTES_IN_HOUER = 60;
    private static final int MILISS_IN_SECCOND = 1000;

    public static double calculatedHoursBetween(LocalDateTime start, LocalDateTime stop) {
        Duration duration = new Duration(start.toDateTime(DateTimeZone.UTC),
                stop.toDateTime(DateTimeZone.UTC));
        int minutes = duration.toStandardMinutes().getMinutes();
        return minutes / 60;
    }

    public static String getHoursFromSeconds(String seconds) {
        try {


            Double seccondsDouble = Double.parseDouble(seconds);
            int seccondsInt = seccondsDouble.intValue();
            LocalTime localTime = LocalTime.fromMillisOfDay(seccondsInt * MILISS_IN_SECCOND);
            return String.format("%d h %d min", localTime.getHourOfDay(), localTime.getMinuteOfHour());
        } catch (NumberFormatException e) {
            return "Cannot parse";
        }
    }

}
