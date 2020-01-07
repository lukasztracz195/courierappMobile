package com.project.courierapp.model.di.clients;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.DeliveryPointsDao;
import com.project.courierapp.model.dtos.request.AddOrEditDeliveryPointRequest;
import com.project.courierapp.model.dtos.request.LocationRequest;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;

import java.util.List;
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
        setValidators();
    }

    public Single<DeliveryPointResponse> addDeliveryPoint(
            AddOrEditDeliveryPointRequest addOrEditDeliveryPointRequest) {
        return async(this.deliveryPointsDao.addDeliveryPoint(addOrEditDeliveryPointRequest)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            response));
                }));
    }

    public Single<DeliveryPointResponse> editDeliveryPoint(
            Long deliveryPointId, AddOrEditDeliveryPointRequest editDeliveryPointRequest) {
        return async(this.deliveryPointsDao.editDeliveryPoint(deliveryPointId,
                editDeliveryPointRequest)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            response));
                }));
    }

    public Single<String> visitDeliveryPoint(Long deliveryPointId, LocationRequest locationRequest) {
        return async(this.deliveryPointsDao.visitDeliveryPoint(deliveryPointId, locationRequest)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            response));
                }));
    }

    public Single<List<DeliveryPointResponse>> getDeliveryPointsByRoadId(Long roadId) {
        return async(this.deliveryPointsDao.getDeliveryPointsByRoadId(roadId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            response));
                }));
    }

    public Single<List<DeliveryPointResponse>> getAllDeliveryPoints() {
        return async(this.deliveryPointsDao.getAllDeliveryPoints()
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            response));
                }));
    }

    public Single<DeliveryPointResponse> getDeliveryPointById(Long deliveryPointId) {
        return async(this.deliveryPointsDao.getDeliveryPointById(deliveryPointId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            response));
                }));
    }

    public Single<Boolean> deleteDeliveryPointById(Long deliveryPointId) {
        return async(this.deliveryPointsDao.deleteDeliveryPointById(deliveryPointId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(true);
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            response));
                }));
    }
}
