package com.project.courierapp.model.di.clients;

import android.util.Log;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.DeliveryPointsDao;
import com.project.courierapp.model.dtos.request.AddDeliveryPointRequest;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.exceptions.http.NotFoundException;
import com.project.courierapp.model.exceptions.http.ServerErrorException;
import com.project.courierapp.model.exceptions.http.UnauthorizedException;

import java.net.HttpURLConnection;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import retrofit2.Retrofit;

import static io.reactivex.Single.error;
import static io.reactivex.Single.just;

public class DeliveryPointsClient extends BaseClient {

    @Named("auth")
    @Inject
    Retrofit retrofit;

    private DeliveryPointsDao deliveryPointsDao;

    public DeliveryPointsClient() {
        CourierApplication.getRetrofitComponent().inject(this);
        this.deliveryPointsDao = retrofit.create(DeliveryPointsDao.class);
    }

    public Single<DeliveryPointResponse> addDeliveryPoint(AddDeliveryPointRequest addDeliveryPointRequest) {
        return async(this.deliveryPointsDao.addDeliveryPoint(addDeliveryPointRequest)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        Log.i("HTTP_UNAUTHORIZED 401", response.message());
                        return error((UnauthorizedException.builder()
                                .message(response.message())
                                .build()));
                    }
                    if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        Log.i("HTTP_NOT_FOUND 404", response.message());
                        return error(NotFoundException.builder()
                        .message(response.message())
                        .build());
                    }
                    return error(ServerErrorException.builder()
                    .message(response.message())
                    .build());
                }));
    }

    public Single<DeliveryPointResponse> editDeliveryPoint(AddDeliveryPointRequest editDeliveryPointRequest) {
        return async(this.deliveryPointsDao.addDeliveryPoint(editDeliveryPointRequest)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        Log.i("HTTP_UNAUTHORIZED 401", response.message());
                        return error((UnauthorizedException.builder()
                                .message(response.message())
                                .build()));
                    }
                    if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        Log.i("HTTP_NOT_FOUND 404", response.message());
                        return error(NotFoundException.builder()
                                .message(response.message())
                                .build());
                    }
                    return error(ServerErrorException.builder()
                            .message(response.message())
                            .build());
                }));
    }

    public Single<Boolean> deleteDeliveryPointById(Long deliveryPointId) {
        return async(this.deliveryPointsDao.deleteDeliveryPointById(deliveryPointId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(true);
                    }
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        Log.i("HTTP_UNAUTHORIZED 401", response.message());
                        return error((UnauthorizedException.builder()
                                .message(response.message())
                                .build()));
                    }
                    if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        Log.i("HTTP_NOT_FOUND 404", response.message());
                        return error(NotFoundException.builder()
                                .message(response.message())
                                .build());
                    }
                    return error(ServerErrorException.builder()
                            .message(response.message())
                            .build());
                }));
    }
}
