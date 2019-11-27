package com.project.sopmmobileapp.model.di.clients;

import android.location.Location;

import com.project.sopmmobileapp.applications.VoteApplication;
import com.project.sopmmobileapp.model.listeners.GpsListener;

import java.util.Optional;

import javax.inject.Inject;

public class GpsClient {

    @Inject
    GpsListener gpsListener;

    public GpsClient() {
        VoteApplication.getClientsComponent().inject(this);
    }

    public Optional<Location> getOptionalLocation() {
        return Optional.of(gpsListener.getLocation());
    }
}
