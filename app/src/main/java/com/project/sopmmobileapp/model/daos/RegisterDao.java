package com.project.sopmmobileapp.model.daos;

import com.project.sopmmobileapp.model.dtos.request.CredentialsRequest;
import com.project.sopmmobileapp.model.dtos.response.BaseResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterDao {
    String BASE_USER_PATH = "/user";
    String REGISTER_PATH = BASE_USER_PATH + "/register";

    @POST(REGISTER_PATH)
    Single<Response<BaseResponse>> register(@Body CredentialsRequest credentialsRequest);

}
