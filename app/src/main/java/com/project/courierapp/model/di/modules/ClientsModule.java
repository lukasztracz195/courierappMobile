package com.project.courierapp.model.di.modules;

import com.project.courierapp.model.di.clients.ChangePasswordClient;
import com.project.courierapp.model.di.clients.DeliveryPointsClient;
import com.project.courierapp.model.di.clients.LoginClient;
import com.project.courierapp.model.di.clients.RegisterClient;
import com.project.courierapp.model.di.clients.RoadClient;
import com.project.courierapp.model.di.clients.TrackingPointsClient;
import com.project.courierapp.model.di.clients.WorkerClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ClientsModule {

    @Singleton
    @Provides
    public LoginClient loginClient() {
        return new LoginClient();
    }

    @Singleton
    @Provides
    public RegisterClient registerClient() {
        return new RegisterClient();
    }

    @Singleton
    @Provides
    public ChangePasswordClient changePasswordClient() {
        return new ChangePasswordClient();
    }

    @Singleton
    @Provides
    public WorkerClient workerClient() {
        return new WorkerClient();
    }


    @Singleton
    @Provides
    public DeliveryPointsClient deliveryPointsClient() {
        return new DeliveryPointsClient();
    }

    @Singleton
    @Provides
    public RoadClient roadClient() {
        return new RoadClient();
    }

    @Singleton
    @Provides
    public TrackingPointsClient trackingPointsClients() {
        return new TrackingPointsClient();
    }
}
