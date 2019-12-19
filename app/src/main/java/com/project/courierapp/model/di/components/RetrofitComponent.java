package com.project.courierapp.model.di.components;

import com.project.courierapp.model.di.clients.ChangePasswordClient;
import com.project.courierapp.model.di.clients.DeliveryPointsClient;
import com.project.courierapp.model.di.clients.LoginClient;
import com.project.courierapp.model.di.clients.RegisterClient;
import com.project.courierapp.model.di.clients.RoadClient;
import com.project.courierapp.model.di.clients.WorkerClient;
import com.project.courierapp.model.di.modules.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RetrofitModule.class})
public interface RetrofitComponent {

    void inject(LoginClient loginClient);

    void inject(RegisterClient registerClient);

    void inject(ChangePasswordClient changePasswordClient);

    void inject(WorkerClient workerClient);

    void inject(DeliveryPointsClient deliveryPointsClient);

    void inject(RoadClient roadClient);
}
