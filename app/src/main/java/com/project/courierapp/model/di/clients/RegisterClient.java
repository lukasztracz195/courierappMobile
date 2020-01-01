package com.project.courierapp.model.di.clients;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.RegisterDao;
import com.project.courierapp.model.dtos.request.RegisterCredentialsRequest;
import com.project.courierapp.model.exceptions.http.ServerErrorException;
import com.project.courierapp.model.exceptions.http.UnauthorizedException;
import com.project.courierapp.model.validators.ValidatorHttpBuilder;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import retrofit2.Retrofit;

import static io.reactivex.Single.error;
import static io.reactivex.Single.just;

public class RegisterClient extends BaseClient {

    @Named("auth")
    @Inject
    Retrofit retrofit;

    private RegisterDao registerDao;

    public RegisterClient() {
        CourierApplication.getRetrofitComponent().inject(this);
        this.registerDao = retrofit.create(RegisterDao.class);
        setValidators();
    }

    public Single<Integer> register(final RegisterCredentialsRequest registerCredentialsRequest) {
        return async(this.registerDao.register(registerCredentialsRequest)
                .flatMap(authenticationResponse -> {
                    if (authenticationResponse.isSuccessful()) {
                        return just(authenticationResponse.code());
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            authenticationResponse));
                }));
    }

    @Override
    public void setValidators() {
        validatorHttpBuilder = ValidatorHttpBuilder.builder()
                .addValidator(HttpURLConnection.HTTP_UNAUTHORIZED, new UnauthorizedException())
                .addValidator(HttpURLConnection.HTTP_SERVER_ERROR, new ServerErrorException());
    }
}
