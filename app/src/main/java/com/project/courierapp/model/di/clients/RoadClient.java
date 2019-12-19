package com.project.courierapp.model.di.clients;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.RoadDao;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Retrofit;

public class RoadClient extends BaseClient {

    @Named("auth")
    @Inject
    Retrofit retrofit;

    private RoadDao roadDao;

    public RoadClient() {
        CourierApplication.getRetrofitComponent().inject(this);
        this.roadDao = retrofit.create(RoadDao.class);
    }
}
