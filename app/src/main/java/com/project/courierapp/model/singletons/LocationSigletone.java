package com.project.courierapp.model.singletons;

import android.location.Location;

import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.fragments.worker_layer.pages_worker.WorkerMapFragment;


public class LocationSigletone {

    private static LocationSigletone instance;
    private Location location;


    private LocationSigletone() {
    }

    public static LocationSigletone getInstance() {
        if (instance == null) {
            instance = new LocationSigletone();
        }
        return instance;
    }

    public void setLocation(Location location) {
        this.location = location;
        if (WorkerMapFragment.instance != null && WorkerMapFragment.mapIsActivated) {
            if (MainActivity.instance != null)
                if(location != null) {
                    MainActivity.instance.runOnUiThread(() ->
                            WorkerMapFragment.instance.updateMap(location));
                }

        }
    }

    public Location getLocation() {
        return location;
    }

}
