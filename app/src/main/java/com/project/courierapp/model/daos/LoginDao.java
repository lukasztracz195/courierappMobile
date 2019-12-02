package com.project.courierapp.model.daos;

import com.project.courierapp.model.dtos.request.CredentialsRequest;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginDao {

    String LOGIN_PATH = "/login";

    @POST(LOGIN_PATH)
    Single<Response<Void>> login(@Body CredentialsRequest credentialsRequest);

}
