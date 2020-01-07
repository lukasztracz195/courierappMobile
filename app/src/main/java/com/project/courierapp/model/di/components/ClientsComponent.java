package com.project.courierapp.model.di.components;

import com.project.courierapp.model.di.modules.ClientsModule;
import com.project.courierapp.model.service.LocationService;
import com.project.courierapp.view.adapters.adapters_manager.AdapterDeliveryPoints;
import com.project.courierapp.view.adapters.adapters_manager.AdapterRoadListItem;
import com.project.courierapp.view.adapters.adapters_manager.AdapterWorkersListItem;
import com.project.courierapp.view.adapters.adapters_worker.AdapterFinishedRoads;
import com.project.courierapp.view.adapters.adapters_worker.AdapterRoadsToStart;
import com.project.courierapp.view.adapters.adapters_worker.AdapterToVisitDeliveryPoints;
import com.project.courierapp.view.fragments.base_layer.ChangePasswordFragment;
import com.project.courierapp.view.fragments.base_layer.LoginFragment;
import com.project.courierapp.view.fragments.manager_layer.functional.CreateDeliveryPointFragment;
import com.project.courierapp.view.fragments.manager_layer.functional.CreateRoadFragment;
import com.project.courierapp.view.fragments.manager_layer.functional.EditDeliveryPointFragment;
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

    void inject(CreateRoadFragment createRoadFragment);

    void inject(AdapterDeliveryPoints adapterDeliveryPoints);

    void inject(CreateDeliveryPointFragment createDeliveryPointFragment);

    void inject(EditDeliveryPointFragment editDeliveryPointFragment);

    void inject(AdapterRoadListItem adapterRoadListItem);

    void inject(AdapterToVisitDeliveryPoints adapterToVisitDeliveryPoints);

    void inject(AdapterRoadsToStart adapterRoadsToStart);

    void inject(AdapterFinishedRoads adapterFinishedRoads);

    void inject(LocationService locationService);
}
