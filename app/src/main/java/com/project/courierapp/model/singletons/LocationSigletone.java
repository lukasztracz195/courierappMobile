package com.project.courierapp.model.singletons;

import android.location.Location;

import com.project.courierapp.model.service.LocationService;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationSigletone {

    private static LocationSigletone instance;
    private Location location;


    private LocationSigletone(){
        if(LocationService.instance != null){
            location = LocationService.instance.getLocation();
        }
    }

    public static LocationSigletone getInstance(){
        if(instance == null){
            instance = new LocationSigletone();
        }
        return instance;
    }


}
