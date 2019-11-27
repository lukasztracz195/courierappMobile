package com.project.sopmmobileapp.model.di.modules;

import com.project.sopmmobileapp.model.di.clients.GpsClient;
import com.project.sopmmobileapp.model.di.clients.LoginClient;
import com.project.sopmmobileapp.model.di.clients.RegisterClient;

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
}
