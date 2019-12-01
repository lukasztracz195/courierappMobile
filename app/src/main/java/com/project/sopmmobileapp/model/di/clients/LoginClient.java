package com.project.sopmmobileapp.model.di.clients;

import android.util.Log;

import com.project.sopmmobileapp.applications.VoteApplication;
import com.project.sopmmobileapp.model.daos.LoginDao;
import com.project.sopmmobileapp.model.dtos.request.CredentialsRequest;
import com.project.sopmmobileapp.model.dtos.response.LoginResponse;
import com.project.sopmmobileapp.model.exceptions.BadRequestException;
import com.project.sopmmobileapp.model.exceptions.LoginException;

import java.net.HttpURLConnection;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import retrofit2.Retrofit;

import static io.reactivex.Single.error;
import static io.reactivex.Single.just;

public class LoginClient extends BaseClient {

    @Named("no_auth")
    @Inject
    Retrofit retrofit;

    private LoginDao loginDao;

    public LoginClient() {
        VoteApplication.getRetrofitComponent().inject(this);
        this.loginDao = retrofit.create(LoginDao.class);
    }

    public Single<String> login(final CredentialsRequest credentialsRequest) {
        return async(this.loginDao.login(credentialsRequest)
                .flatMap(authenticationResponse -> {
                    Log.i("HEADERS_HTTP", authenticationResponse.headers().get("Authorization"));
                    if (authenticationResponse.isSuccessful()) {
                        Log.i("HEADERS_HTTP", authenticationResponse.headers().toString());
                        return just(Objects.requireNonNull(Objects.
                                requireNonNull(authenticationResponse.headers()
                                        .get("Authorization")).split(" ")[1]));
                    }
                    if (authenticationResponse.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        return error((new LoginException()));
                    }
                    if (authenticationResponse.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        return error(new BadRequestException());
                    }
                    return error(new RuntimeException(Objects.requireNonNull(authenticationResponse.errorBody()).toString()));
                }));
    }
}
