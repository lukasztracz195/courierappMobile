package com.project.courierapp.model.di.clients;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.ChangePasswordDao;
import com.project.courierapp.model.dtos.request.ChangePasswordRequest;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import retrofit2.Retrofit;

import static io.reactivex.Single.error;
import static io.reactivex.Single.just;

public class ChangePasswordClient extends BaseClient{

    @Named("auth")
    @Inject
    Retrofit retrofit;

    private ChangePasswordDao changePasswordDao;

    public ChangePasswordClient() {
        CourierApplication.getRetrofitComponent().inject(this);
        this.changePasswordDao = retrofit.create(ChangePasswordDao.class);
        setValidators();
    }

    public Single<Void> changePassword(final String login,
                                       final ChangePasswordRequest changePasswordRequest) {
        return async(this.changePasswordDao.changePassword(login, changePasswordRequest)
                .flatMap(authenticationResponse -> {
                    if (authenticationResponse.isSuccessful()) {
                        return just(Objects.requireNonNull(authenticationResponse.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            authenticationResponse));
                }));
    }
}
