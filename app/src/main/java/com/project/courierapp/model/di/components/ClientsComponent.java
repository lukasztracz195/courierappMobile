package com.project.courierapp.model.di.components;

import com.project.courierapp.model.di.clients.GpsClient;
import com.project.courierapp.model.di.modules.ClientsModule;
import com.project.courierapp.view.fragments.LoginFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ClientsModule.class})
public interface ClientsComponent {

    void inject(LoginFragment loginFragment);

    void inject(GpsClient gpsClient);
}
