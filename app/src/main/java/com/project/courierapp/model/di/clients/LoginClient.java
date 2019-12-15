package com.project.courierapp.model.di.clients;

import android.util.Log;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.LoginDao;
import com.project.courierapp.model.dtos.request.CredentialsRequest;
import com.project.courierapp.model.exceptions.http.BadRequestException;
import com.project.courierapp.model.exceptions.LoginException;

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
        CourierApplication.getRetrofitComponent().inject(this);
        this.loginDao = retrofit.create(LoginDao.class);
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
                    if (authenticationResponse.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        Log.i("HTTP_UNAUTHORIZED 401", authenticationResponse.message());
                        return error((new LoginException()));
                    }
                    if (authenticationResponse.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        Log.i("HTTP_NOT_FOUND 404", authenticationResponse.message());
                        return error(new BadRequestException());
                    }
                    return error(new RuntimeException(Objects.requireNonNull(authenticationResponse.errorBody()).toString()));
                }));
    }
}
