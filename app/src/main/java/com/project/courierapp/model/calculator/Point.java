package com.project.courierapp.model.calculator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Point {

    private final double latitude;

    private final double longitude;

    private Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Point of(double latitude, double longitude) {
        return new Point(latitude, longitude);
    }


}