package com.project.courierapp.model.di.clients;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.TrackingPointsDao;
import com.project.courierapp.model.dtos.request.AddTrackingPointRequest;
import com.project.courierapp.model.dtos.response.TrackingPointsResponse;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import retrofit2.Retrofit;

import static io.reactivex.Single.error;
import static io.reactivex.Single.just;

public class TrackingPointsClient extends BaseClient {
    @Named("auth")
    @Inject
    Retrofit retrofit;

    private TrackingPointsDao trackingPointsDao;

    public TrackingPointsClient() {
        CourierApplication.getRetrofitComponent().inject(this);
        this.trackingPointsDao = retrofit.create(TrackingPointsDao.class);
        setValidators();
    }

    public Single<TrackingPointsResponse> addTrackingPointResponse(Long roadId,
                                                                   AddTrackingPointRequest addTrackingPointRequest) {
        return async(trackingPointsDao.addTrackingPointResponse(roadId, addTrackingPointRequest)
                .flatMap(trackingPointResponse -> {
                    if (trackingPointResponse.isSuccessful()) {
                        return just(Objects.requireNonNull(trackingPointResponse.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            trackingPointResponse));
                }));
    }

    public Single<List<TrackingPointsResponse>> getByRoadIdTrackingResponses(Long roadId) {
        return async(trackingPointsDao.getByRoadId(roadId)
                .flatMap(trackingPointResponse -> {
                    if (trackingPointResponse.isSuccessful()) {
                        return just(Objects.requireNonNull(trackingPointResponse.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            trackingPointResponse));
                }));
    }

    public Single<List<TrackingPointsResponse>> getTraceStartedRoadByLoggedWorker() {
        return async(trackingPointsDao.getTraceStartedRoadByLoggedWorker()
                .flatMap(trackingPointResponse -> {
                    if (trackingPointResponse.isSuccessful()) {
                        return just(Objects.requireNonNull(trackingPointResponse.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),
                            trackingPointResponse));
                }));
    }
}
