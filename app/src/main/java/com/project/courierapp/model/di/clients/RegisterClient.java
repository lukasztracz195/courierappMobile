package com.project.courierapp.model.di.clients;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.RegisterDao;
import com.project.courierapp.model.dtos.request.CredentialsRequest;
import com.project.courierapp.model.dtos.response.BaseResponse;
import com.project.courierapp.model.exceptions.UserIsTakenException;

import java.net.HttpURLConnection;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import retrofit2.Retrofit;

import static io.reactivex.Single.error;
import static io.reactivex.Single.just;

public class RegisterClient extends BaseClient {

    @Named("no_auth")
    @Inject
    Retrofit retrofit;

    private RegisterDao registerDao;

    public RegisterClient() {
        CourierApplication.getRetrofitComponent().inject(this);
        this.registerDao = retrofit.create(RegisterDao.class);
    }

    public Single<BaseResponse> register(final CredentialsRequest credentialsRequest) {
        return async(this.registerDao.register(credentialsRequest)
                .flatMap(authenticationResponse -> {
                    if (authenticationResponse.isSuccessful()) {
                        return just(Objects.requireNonNull(authenticationResponse.body()));
                    }
                    if (authenticationResponse.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        return error(new UserIsTakenException());
                    }
                    return error(new RuntimeException(Objects.requireNonNull(authenticationResponse.errorBody()).toString()));
                }));
    }
}
