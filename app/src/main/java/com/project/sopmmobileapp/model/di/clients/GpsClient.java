package com.project.sopmmobileapp.model.di.clients;

import android.location.Location;

import com.project.sopmmobileapp.applications.CourierApplication;
import com.project.sopmmobileapp.model.listeners.GpsListener;

import java.util.Optional;

import javax.inject.Inject;

public class GpsClient {

    @Inject
    GpsListener gpsListener;

    public GpsClient() {
        CourierApplication.getClientsComponent().inject(this);
    }

    public Optional<Location> getOptionalLocation() {
        return Optional.of(gpsListener.getLocation());
    }
}
