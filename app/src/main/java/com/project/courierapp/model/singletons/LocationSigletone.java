package com.project.courierapp.model.singletons;

import android.location.Location;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationSigletone {

    private static LocationSigletone instance;
    private Location location;


    private LocationSigletone(){
    }

    public static LocationSigletone getInstance(){
        if(instance == null){
            instance = new LocationSigletone();
        }
        return instance;
    }


}
