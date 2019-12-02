package com.project.courierapp.model.daos;

import com.project.courierapp.model.dtos.request.ChangePasswordRequest;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChangePasswordDao {
    String CHANGE_PASSWORD = "/changePassword/{login}";

    @POST(CHANGE_PASSWORD)
    Single<Response<Void>> changePassword(@Path("login") String login,
                                          @Body ChangePasswordRequest changePasswordRequest);
}
