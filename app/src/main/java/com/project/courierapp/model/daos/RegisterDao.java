package com.project.courierapp.model.daos;

import com.project.courierapp.model.dtos.request.RegisterCredentialsRequest;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterDao {
    String REGISTER_PATH = "/register";

    @POST(REGISTER_PATH)
    Single<Response<Integer>> register(@Body RegisterCredentialsRequest registerCredentialsRequest);

}
