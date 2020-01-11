package com.project.courierapp.model.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.calculator.DistanceCalculator;
import com.project.courierapp.model.di.clients.TrackingPointsClient;
import com.project.courierapp.model.dtos.request.AddTrackingPointRequest;
import com.project.courierapp.model.enums.DistanceUnits;
import com.project.courierapp.model.store.LastStartedRoadStore;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.toasts.ToastFactory;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LocationService extends Service {

    public static LocationService instance;
    private static final String TAG = LocationService.class.getSimpleName();
    private FusedLocationProviderClient fusedLocationClient;
    private Location oldLocation;
    private Timer timer;
    private TimerTask timerTask;
    private boolean sendingTrackingPointsIsActivated = false;
    private static final long PERIOD = 1000 * 60; // 1min
    private static final long DELAY = 0; // 1s
    private static final double RANGE_IN_METERS = 50;
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    TrackingPointsClient trackingPointsClient;

    @Override
    public void onCreate() {
        super.onCreate();
        CourierApplication.getClientsComponent().inject(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.instance,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            instance = this;
        }
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
        return super.onStartCommand(intent, flags, startId);
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
            @SuppressLint("CheckResult")
            public void run() {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MainActivity.instance, newLocation -> {
                            if (newLocation != null) {
                                oldLocation = newLocation;
                                logLocation(oldLocation);
//                                toastLocation(oldLocation);
                            }
                        });
                Long startedRoadId = LastStartedRoadStore.getLastStartedRoadId();
                if (startedRoadId > 0 && oldLocation != null) {
                    Disposable disposable = trackingPointsClient.addTrackingPointResponse(startedRoadId,
                            AddTrackingPointRequest.builder()
                                    .latitude(oldLocation.getLatitude())
                                    .longitude(oldLocation.getLongitude())
                                    .build()).subscribe(response -> {
                        if (response != null) {
                            ToastFactory.createToast(MainActivity.instance,
                                    "Created trackingPoint");
                        }
                    }, (Throwable e) -> {
                        Log.i(TAG, "Created tracking point");
                    });
                    compositeDisposable.add(disposable);
                }
            }
        };
    }

    private void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
            instance = null;
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

    private void toastLocation(Location location) {
        ToastFactory.createToast(MainActivity.instance,
                "Current location lng: " +
                        location.getLongitude() +
                        " lat: " +
                        location.getLatitude());
    }
}
