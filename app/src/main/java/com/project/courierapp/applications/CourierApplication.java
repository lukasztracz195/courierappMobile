package com.project.courierapp.applications;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.project.courierapp.model.di.components.ClientsComponent;
import com.project.courierapp.model.di.components.DaggerClientsComponent;
import com.project.courierapp.model.di.components.DaggerRetrofitComponent;
import com.project.courierapp.model.di.components.RetrofitComponent;

public class CourierApplication extends Application {

    private static RetrofitComponent retrofitComponent;

    private static ClientsComponent clientsComponent;

    private static boolean activityVisible;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofitComponent = this.createRetrofitComponent();
        clientsComponent = this.createClientsComponent();
        context = super.getApplicationContext();
    }

    private RetrofitComponent createRetrofitComponent() {
        return DaggerRetrofitComponent.create();
    }

    public static RetrofitComponent getRetrofitComponent() {
        return retrofitComponent;
    }

    private ClientsComponent createClientsComponent() {
        return DaggerClientsComponent.create();
    }

    public static ClientsComponent getClientsComponent() {
        return clientsComponent;
    }

    public static Context getContext() {
        return context;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }
}
