package com.project.courierapp.model.di.components;

import com.project.courierapp.model.di.modules.ClientsModule;
import com.project.courierapp.view.adapters.adapters_manager.AdapterWorkersListItem;
import com.project.courierapp.view.fragments.base_layer.ChangePasswordFragment;
import com.project.courierapp.view.fragments.base_layer.LoginFragment;
import com.project.courierapp.view.fragments.manager_layer.functional.CreateDeliveryPointsFragment;
import com.project.courierapp.view.fragments.manager_layer.functional.RegisterWorkerFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ClientsModule.class})
public interface ClientsComponent {

    void inject(LoginFragment loginFragment);

    void inject(ChangePasswordFragment changePasswordFragment);

    void inject(AdapterWorkersListItem adapterWorkersListItem);

    void inject(RegisterWorkerFragment registerWorkerFragment);

    void inject(CreateDeliveryPointsFragment createDeliveryPointsFragment);
}
