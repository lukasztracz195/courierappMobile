package com.project.sopmmobileapp.model.daos;

import com.project.sopmmobileapp.model.dtos.request.CredentialsRequest;
import com.project.sopmmobileapp.model.dtos.response.LoginResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginDao {

    String LOGIN_PATH = "/login";

    @POST(LOGIN_PATH)
    Single<Response<Void>> login(@Body CredentialsRequest credentialsRequest);

}
