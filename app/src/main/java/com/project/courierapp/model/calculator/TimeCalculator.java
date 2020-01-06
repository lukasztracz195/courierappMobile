package com.project.courierapp.model.calculator;

import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;

public class TimeCalculator {

    public static double calculatedHoursBetween(LocalDateTime start, LocalDateTime stop) {
        Duration duration = new Duration(start.toDateTime(DateTimeZone.UTC),
                stop.toDateTime(DateTimeZone.UTC));
        int minutes = duration.toStandardMinutes().getMinutes();
        return minutes / 60;
    }
}
