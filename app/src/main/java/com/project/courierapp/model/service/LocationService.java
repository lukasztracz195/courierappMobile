package com.project.courierapp.model.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.calculator.DistanceCalculator;
import com.project.courierapp.model.di.clients.TrackingPointsClient;
import com.project.courierapp.model.dtos.request.AddTrackingPointRequest;
import com.project.courierapp.model.enums.DistanceUnits;
import com.project.courierapp.model.singletons.LocationSigletone;
import com.project.courierapp.model.store.LastStartedRoadStore;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.toasts.ToastFactory;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LocationService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static LocationService instance;
    private static final String TAG = LocationService.class.getSimpleName();
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private Timer timer;
    private TimerTask timerTask;
    private boolean sendingTrackingPointsIsActivated = false;
    private static final long PERIOD = 1000; // 1 sec
    private static final long PERIOD_FOR_TRACKING_POINTS = 1000 * 60;
    private static final long DELAY = 0; // 0s
    private static final double RANGE_IN_METERS = 1;
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
            int locationRequestCode = 1000;
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
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("You are been followed by your employer")
                .setContentText(input)
                .setSmallIcon(R.drawable.you_are_been_followed_icon)
                .build();
        startForeground(1, notification);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
        Log.i(TAG, "LocationService Stopped!");
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
                Location latestLocation = currentLocation;
                getLastLocation();
                if (CourierApplication.isActivityVisible()) {
                    LocationSigletone.getInstance().setLocation(currentLocation);
                    logLocation(currentLocation);
                }
                Long startedRoadId = LastStartedRoadStore.getLastStartedRoadId();
                if (startedRoadId > 0 && currentLocation != null) {
                    if (toFar(latestLocation, currentLocation)) {
                        Disposable disposable = trackingPointsClient.addTrackingPointResponse(startedRoadId,
                                AddTrackingPointRequest.builder()
                                        .latitude(currentLocation.getLatitude())
                                        .longitude(currentLocation.getLongitude())
                                        .build()).subscribe(response -> {
//                            if (response != null) {
////                            ToastFactory.createToast(MainActivity.instance,
////                                    "Created trackingPoint");
//                            }
                        }, (Throwable e) -> {
                            Log.i(TAG, "Not created tracking point");
                        });
                        compositeDisposable.add(disposable);
                    }
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
        if(location != null) {
            Log.i(TAG,
                    "Current location lng: " +
                            location.getLongitude() +
                            " lat: " +
                            location.getLatitude());
        }
    }

    private static boolean toFar(Location source, Location destination) {
        double distance = DistanceCalculator
                .calculateDistance(source, destination, DistanceUnits.METERS);
        Log.i(TAG, "Distance beetwen last locations = "+ distance+" m");
        return (distance >= RANGE_IN_METERS);
    }

    private void toastLocation(Location location) {
        if(location != null) {
            ToastFactory.createToast(MainActivity.instance,
                    "Current location lng: " +
                            location.getLongitude() +
                            " lat: " +
                            location.getLatitude());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void getLastLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                currentLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }
}
