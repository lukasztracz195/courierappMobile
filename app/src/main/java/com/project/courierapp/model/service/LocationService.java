package com.project.courierapp.model.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.project.courierapp.model.calculator.DistanceCalculator;
import com.project.courierapp.model.enums.DistanceUnits;
import com.project.courierapp.view.activities.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service {

    private static final String TAG = LocationService.class.getSimpleName();
    private FusedLocationProviderClient fusedLocationClient;
    private Location oldLocation;
    private Timer timer;
    private TimerTask timerTask;
    private boolean sendingTrackingPointsIsActivated = false;
    private static final long PERIOD = 1000 * 60 * 5; // 5min
    private static final long DELAY = 1000; // 1s
    private static final double RANGE_IN_METERS = 50;


    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "LocationService Started!");
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
        Log.i(TAG, "LocationService Stopped!");
    }

    public Location getLocation() {
        return oldLocation;
    }

    public boolean isSendingTrackingPointsIsActivated() {
        return sendingTrackingPointsIsActivated;
    }

    public void setSendingTrackingPointsIsActivated(boolean sendingTrackingPointsIsActivated) {
        this.sendingTrackingPointsIsActivated = sendingTrackingPointsIsActivated;
    }


    private void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, DELAY, PERIOD); //
    }

    private void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MainActivity.instance, newLocation -> {
                            if (newLocation != null) {
                                oldLocation = newLocation;
                                logLocation(oldLocation);
                            }
                        });
                if (sendingTrackingPointsIsActivated) {
                    //TODO SEND TRACKING POINTS
                }
            }
        };
    }

    private void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void logLocation(Location location) {
        Log.i(TAG,
                "Current location lng: " +
                        location.getLongitude() +
                        " lat: " +
                        location.getLatitude());
    }

    private static boolean toFar(Location source, Location destination) {
        double distance = DistanceCalculator
                .calculateDistance(source, destination, DistanceUnits.METERS);
        return (distance >= RANGE_IN_METERS);
    }


}
