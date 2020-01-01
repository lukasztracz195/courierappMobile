package com.project.courierapp.model.di.clients;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.RoadDao;
import com.project.courierapp.model.dtos.request.AddRoadRequest;
import com.project.courierapp.model.dtos.request.LocationRequest;
import com.project.courierapp.model.dtos.response.RoadResponse;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import retrofit2.Retrofit;

import static io.reactivex.Single.error;
import static io.reactivex.Single.just;

public class RoadClient extends BaseClient {

    @Named("auth")
    @Inject
    Retrofit retrofit;

    private RoadDao roadDao;

    public RoadClient() {
        CourierApplication.getRetrofitComponent().inject(this);
        this.roadDao = retrofit.create(RoadDao.class);
        setValidators();
    }


    public Single<RoadResponse> add(AddRoadRequest addRoadRequest) {
        return async(this.roadDao.add(addRoadRequest)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }

    public Single<RoadResponse> startRoad(Long roadId, LocationRequest
            locationRequest) {
        return async(this.roadDao.startRoad(roadId, locationRequest)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }


    public Single<RoadResponse> finishRoad(Long roadId, LocationRequest
            locationRequest) {
        return async(this.roadDao.finishRoad(roadId, locationRequest)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }

    public Single<RoadResponse> getRoadByWorkerId(Long workerId) {
        return async(this.roadDao.getRoadByWorkerId(workerId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }

    public Single<List<RoadResponse>> getAllPlannedRoads() {
        return async(this.roadDao.getAllPlannedRoads()
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }

    public Single<List<RoadResponse>> getPlannedRoadsByWorkerId(Long workerId) {
        return async(this.roadDao.getPlannedRoadsByWorkerId(workerId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }

    public Single<List<RoadResponse>> getAllStartedRoads() {
        return async(this.roadDao.getAllStartedRoads()
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }

    public Single<List<RoadResponse>> getStartedRoadsByWorkerId(Long workerId) {
        return async(this.roadDao.getStartedRoadsByWorkerId(workerId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }

    public Single<List<RoadResponse>> getAllFinishedRoads() {
        return async(this.roadDao.getAllFinishedRoads()
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }

    public Single<List<RoadResponse>> getFinishedRoadsByWorkerId(Long workerId) {
        return async(this.roadDao.getFinishedRoadsByWorkerId(workerId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }

    public Single<List<RoadResponse>> getAllPlannedRoadForLoggedWorker() {
        return async(this.roadDao.getAllPlannedRoadForLoggedWorker()
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }

    public Single<List<RoadResponse>> getAllFinishedRoadForLoggedWorker() {
        return async(this.roadDao.getAllFinishedRoadForLoggedWorker()
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }



    public Single<List<RoadResponse>> getAllRoads() {
        return async(this.roadDao.getAllRoads()
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }

    public Single<RoadResponse> getLastStartedRoad() {
        return async(this.roadDao.getLastStartedRoad()
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }

    public Single<Boolean> deleteRoadById(Long roadId) {
        return async(this.roadDao.deleteRoadById(roadId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(), response));
                }));
    }
}
