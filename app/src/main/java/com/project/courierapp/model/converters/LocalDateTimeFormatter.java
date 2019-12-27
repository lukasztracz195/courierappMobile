package com.project.courierapp.model.converters;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {

    public static String format(LocalDateTime localDateTime){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("d-MMMM-yyyy hh:mm:ss");
        return localDateTime.toString(fmt);
    }
}
