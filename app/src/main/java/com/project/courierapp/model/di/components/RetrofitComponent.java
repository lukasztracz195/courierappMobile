package com.project.courierapp.model.di.components;

import com.project.courierapp.model.di.clients.LoginClient;
import com.project.courierapp.model.di.clients.RegisterClient;
import com.project.courierapp.model.di.modules.RetrofitModule;
import com.project.courierapp.model.di.modules.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RetrofitModule.class})
public interface RetrofitComponent {

    void inject(LoginClient loginClient);

    void inject(RegisterClient registerClient);
}
