package com.project.sopmmobileapp.model.listeners;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.project.sopmmobileapp.applications.VoteApplication;
import com.project.sopmmobileapp.model.exceptions.GpsException;
import com.project.sopmmobileapp.view.activities.MainActivity;

import javax.inject.Inject;

public class GpsListener implements LocationListener {
    private Context context;
    private Activity activity;
    private Location location;
    private static final int LOCATION_REFRESH_TIME_MSEC = 5000;
    private static final int LOCATION_REFRESH_DISTANCE_METERS = 100;
    private static final int REQUEST_CODE = 123;
    private String TAG = this.getClass().getName();

    @Inject
    public GpsListener() {
        this.context = VoteApplication.getContext();
        this.activity = MainActivity.instance;
        ActivityCompat.requestPermissions(activity, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        init();
    }

    private void init() {
        String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "GPS permission not granted");
        }
        try {
            LocationManager lm = (LocationManager)
                    context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                Log.i(TAG, "GPS enabled");
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_REFRESH_TIME_MSEC,
                        LOCATION_REFRESH_DISTANCE_METERS,
                        this);
            } else {
                Log.w(TAG, "GPS not enabled");
            }
        } catch (GpsException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
//        Toast.makeText(context.getApplicationContext(), location.getLatitude() + " " + location.getLongitude(),
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public Location getLocation() {
        return location;
    }
}