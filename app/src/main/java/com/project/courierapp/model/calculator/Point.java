package com.project.courierapp.model.calculator;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

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

    public static Point of(LatLng latLng){
        return new Point(latLng.latitude, latLng.longitude);
    }
    public static Point of(Location location){
        return new Point(location.getLatitude(), location.getLongitude());
    }


}