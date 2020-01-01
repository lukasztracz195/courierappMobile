package com.project.courierapp.model.di.clients;

import android.util.Log;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.LoginDao;
import com.project.courierapp.model.dtos.request.CredentialsRequest;

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
        CourierApplication.getRetrofitComponent().inject(this);
        this.loginDao = retrofit.create(LoginDao.class);
        setValidators();
    }

    public Single<String> login(final CredentialsRequest credentialsRequest) {
        credentialsRequest.setPassword(credentialsRequest.getPassword().trim());
        return async(this.loginDao.login(credentialsRequest)
                .flatMap(authenticationResponse -> {
                    if (authenticationResponse.isSuccessful()) {
                        Log.i("HEADERS_HTTP", authenticationResponse.headers().toString());
                        return just(Objects.requireNonNull(Objects.
                                requireNonNull(authenticationResponse.headers()
                                        .get("Authorization")).split(" ")[1]));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            authenticationResponse));
                }));
    }
}
