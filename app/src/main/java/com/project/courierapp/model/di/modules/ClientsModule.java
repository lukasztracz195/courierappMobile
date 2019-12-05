package com.project.courierapp.model.di.modules;

import com.project.courierapp.model.di.clients.ChangePasswordClient;
import com.project.courierapp.model.di.clients.GpsClient;
import com.project.courierapp.model.di.clients.LoginClient;
import com.project.courierapp.model.di.clients.RegisterClient;
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
    public GpsClient gpsClient() {
        return new GpsClient();
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
}
