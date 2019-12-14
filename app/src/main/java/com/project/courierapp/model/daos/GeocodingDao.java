package com.project.courierapp.model.daos;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.dtos.request.CredentialsRequest;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface GeocodingDao {



     String GOOFLE_GEO_API_KEY = CourierApplication.getContext().getString(R.string.api_key);
     String GEO_PATH =
     String
    @GET(GEO_PATh)
    Single<Response<Void>> login(@Body CredentialsRequest credentialsRequest);
}
