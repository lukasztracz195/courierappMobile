package com.project.courierapp.model.converters;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;
import com.project.courierapp.model.calculator.Point;

public class LocationConverter {

    public static Location pointToLocation(Point point) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(point.getLatitude());
        location.setLongitude(point.getLongitude());
        return location;
    }

    public static Location latLngToLocation(LatLng latLng) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    public static LatLng locationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static LatLng pointToLatLng(Point point) {
        return new LatLng(point.getLatitude(), point.getLongitude());
    }

    public static Point locationToPoint(Location location) {
        return Point.of(location.getLatitude(), location.getLongitude());
    }

    public static Point latLngToPoint(LatLng latLng) {
        return Point.of(latLng.latitude, latLng.longitude);
    }
}
