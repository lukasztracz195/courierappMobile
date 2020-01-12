package com.project.courierapp.model.singletons;

import android.location.Location;

import com.project.courierapp.model.observer.LocationObserver;
import com.project.courierapp.model.observer.LocationSubscriber;
import com.project.courierapp.view.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class LocationSigletone implements LocationObserver {

    private static LocationSigletone instance;
    private Location location;
    private List<LocationSubscriber> subscribers;


    private LocationSigletone() {
        subscribers = new ArrayList<>();
    }

    public static LocationSigletone getInstance() {
        if (instance == null) {
            instance = new LocationSigletone();
        }
        return instance;
    }

    public void setLocation(Location location) {
        this.location = location;
        if (MainActivity.instance != null)
            if (location != null) {
                MainActivity.instance.runOnUiThread(() ->
                        notifyAll(location));
            }

    }

    public Location getLocation() {
        return location;
    }

    @Override
    public void notifyAll(Location location) {
        for (LocationSubscriber subscriber : subscribers) {
            subscriber.notifyByLocation(location);
        }
    }

    public void addSubscriber(LocationSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(LocationSubscriber subscriber) {
        subscribers.remove(subscriber);
    }
}
