package com.project.sopmmobileapp.model.di.clients;

import com.project.sopmmobileapp.applications.VoteApplication;
import com.project.sopmmobileapp.model.daos.RegisterDao;
import com.project.sopmmobileapp.model.dtos.request.CredentialsRequest;
import com.project.sopmmobileapp.model.dtos.response.BaseResponse;
import com.project.sopmmobileapp.model.exceptions.UserIsTakenException;

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
        VoteApplication.getRetrofitComponent().inject(this);
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
