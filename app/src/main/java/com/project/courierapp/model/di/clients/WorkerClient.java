package com.project.courierapp.model.di.clients;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.WorkerDao;
import com.project.courierapp.model.dtos.response.IsBusyResponse;
import com.project.courierapp.model.dtos.response.WorkerResponse;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import retrofit2.Retrofit;

import static io.reactivex.Single.error;
import static io.reactivex.Single.just;

public class WorkerClient extends BaseClient {

    @Named("auth")
    @Inject
    Retrofit retrofit;

    private WorkerDao workerDao;

    public WorkerClient() {
        CourierApplication.getRetrofitComponent().inject(this);
        this.workerDao = retrofit.create(WorkerDao.class);
        setValidators();
    }

    public Single<List<WorkerResponse>> getWorkers() {
        return async(this.workerDao.getWorkers()
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),response));
                }));
    }

    public Single<Void> lockWorker(Long workerId) {
        return async(this.workerDao.lockWorker(workerId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),response));
                }));
    }

    public Single<Void> unlockWorker(Long workerId) {
        return async(this.workerDao.unlockWorker(workerId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),response));
                }));
    }

    public Single<IsBusyResponse> isBusy() {
        return async(this.workerDao.isBusy()
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    return error(validatorHttpBuilder.validate(this.getClass().getName(),response));
                }));
    }
}
