package com.project.courierapp.model.converters;

public class DurationConverter {

    public static String formatDuration(long minutes) {
        long hours = minutes / 60;
        long minutesBetweenHour = minutes - (hours * 60);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PT");
        if(hours >= 0){
            stringBuilder.append(hours);
            stringBuilder.append("H");
        }
        if(minutesBetweenHour > 0){
            stringBuilder.append(minutesBetweenHour);
            stringBuilder.append("M");
        }
        if(minutes == 0 && hours == 0){
            return "PT0S";
        }
        return stringBuilder.toString();
    }

}
