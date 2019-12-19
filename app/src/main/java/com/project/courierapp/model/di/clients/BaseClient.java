package com.project.courierapp.model.di.clients;

import com.project.courierapp.model.exceptions.http.BadRequestException;
import com.project.courierapp.model.exceptions.http.NotFoundException;
import com.project.courierapp.model.exceptions.http.ServerErrorException;
import com.project.courierapp.model.exceptions.http.UnauthorizedException;
import com.project.courierapp.model.validators.ValidatorHttpBuilder;

import java.net.HttpURLConnection;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class BaseClient{

    protected ValidatorHttpBuilder validatorHttpBuilder;

    <T> Single<T> async(Single<T> single) {
        return single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void setValidators() {
        validatorHttpBuilder = ValidatorHttpBuilder.builder()
                .addValidator(HttpURLConnection.HTTP_UNAUTHORIZED, new UnauthorizedException())
                .addValidator(HttpURLConnection.HTTP_BAD_REQUEST, new BadRequestException())
                .addValidator(HttpURLConnection.HTTP_NOT_FOUND, new NotFoundException())
                .addValidator(HttpURLConnection.HTTP_SERVER_ERROR, new ServerErrorException());
    }
}
