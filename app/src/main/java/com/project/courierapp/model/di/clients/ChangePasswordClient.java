package com.project.courierapp.model.di.clients;

import android.util.Log;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.ChangePasswordDao;
import com.project.courierapp.model.daos.LoginDao;
import com.project.courierapp.model.dtos.request.ChangePasswordRequest;
import com.project.courierapp.model.dtos.request.CredentialsRequest;
import com.project.courierapp.model.exceptions.BadRequestException;
import com.project.courierapp.model.exceptions.LoginException;

import java.net.HttpURLConnection;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import retrofit2.Retrofit;

import static io.reactivex.Single.error;
import static io.reactivex.Single.just;

public class ChangePasswordClient extends BaseClient {

    @Named("auth")
    @Inject
    Retrofit retrofit;

    private ChangePasswordDao changePasswordDao;

    public ChangePasswordClient() {
        CourierApplication.getRetrofitComponent().inject(this);
        this.changePasswordDao = retrofit.create(ChangePasswordDao.class);
    }

    public Single<Void> changePassword(final String login, final ChangePasswordRequest changePasswordRequest) {
        return async(this.changePasswordDao.changePassword(login, changePasswordRequest)
                .flatMap(authenticationResponse -> {
                    if (authenticationResponse.isSuccessful()) {
                        return just(Objects.requireNonNull(authenticationResponse.body()));
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
