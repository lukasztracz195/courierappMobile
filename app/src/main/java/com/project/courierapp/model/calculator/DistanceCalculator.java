package com.project.courierapp.model.calculator;

import android.location.Location;

import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.enums.DistanceUnits;

import java.util.List;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public class DistanceCalculator {

    private static final double MILE = 1.1515;
    private static final double KILOMETER = 1.609344;

    public static double calculateDistance(Location source, Location destination, DistanceUnits distanceUnits) {
        if (source.equals(destination)) {
            return 0;
        }
        switch (distanceUnits) {
            case MILES: {
                return calculateInMiles(source, destination);
            }
            case KILOMETERS: {
                double distance = calculateInMiles(source, destination);
                return (distance * KILOMETER);
            }
            case METERS:{
                double distance = calculateInMiles(source, destination);
                return (distance * KILOMETER) * 1_000;
            }
        }
        return 0;
    }

    public static double calculateDistance(Point source, Point destination, DistanceUnits distanceUnits) {
        if (source.equals(destination)) {
            return 0;
        }
        switch (distanceUnits) {
            case MILES: {
                return calculateInMiles(source, destination);
            }
            case KILOMETERS: {
                double distance = calculateInMiles(source, destination);
                return (distance * KILOMETER);
            }
            case METERS:{
                double distance = calculateInMiles(source, destination);
                return (distance * KILOMETER) * 1_000;
            }
        }
        return 0;
    }

    private static double calculateInMiles(Location source, Location destination) {
        double theta = source.getLongitude() - destination.getLongitude();
        double dist = sin(toRadians(source.getLatitude())) * sin(toRadians(destination.getLatitude())) +
                cos(toRadians(source.getLatitude())) * cos(toRadians(destination.getLatitude())) * cos(toRadians(theta));
        dist = acos(dist);
        dist = toDegrees(dist);
        return dist * 60 * MILE;
    }

    private static double calculateInMiles(Point source, Point destination) {
        double theta = source.getLongitude() - destination.getLongitude();
        double dist = sin(toRadians(source.getLatitude())) * sin(toRadians(destination.getLatitude())) +
                cos(toRadians(source.getLatitude())) * cos(toRadians(destination.getLatitude())) * cos(toRadians(theta));
        dist = acos(dist);
        dist = toDegrees(dist);
        return dist * 60 * MILE;
    }

    public static double caluculateDistanceFromListDeliveryPoints(List<DeliveryPointResponse> sortedByOrderDeliveryPoints){
        double distance = 0.0;
        for (int i = 1; i < sortedByOrderDeliveryPoints.size(); i++) {
            DeliveryPointResponse sourceDeliveryPoint = sortedByOrderDeliveryPoints.get(i - 1);
            DeliveryPointResponse destinationDeliveryPoint = sortedByOrderDeliveryPoints.get(i);
            Point source = Point.of(
                    sourceDeliveryPoint.getLatitude(),
                    sourceDeliveryPoint.getLongitude());
            Point destination = Point.of(
                    destinationDeliveryPoint.getLatitude(),
                    destinationDeliveryPoint.getLongitude());
            distance += DistanceCalculator.calculateDistance(source, destination,
                    DistanceUnits.KILOMETERS);
        }
        return distance;
    }
}
