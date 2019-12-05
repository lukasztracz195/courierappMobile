package com.project.courierapp.model.di.clients;

import android.util.Log;

import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.daos.WorkerDao;
import com.project.courierapp.model.dtos.response.WorkerResponse;
import com.project.courierapp.model.exceptions.BadRequestException;
import com.project.courierapp.model.exceptions.UnauthorizedException;

import java.net.HttpURLConnection;
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
    }

    public Single<List<WorkerResponse>> getWorkers() {
        return async(this.workerDao.getWorkers()
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        Log.i("HTTP_UNAUTHORIZED 401", response.message());
                        return error((new UnauthorizedException()));
                    }
                    if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        Log.i("HTTP_NOT_FOUND 404", response.message());
                        return error(new BadRequestException());
                    }
                    return error(new RuntimeException(Objects.requireNonNull(response.errorBody()).toString()));
                }));
    }

    public Single<Void> lockWorker(Long workerId) {
        return async(this.workerDao.lockWorker(workerId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        Log.i("HTTP_UNAUTHORIZED 401", response.message());
                        return error((new UnauthorizedException()));
                    }
                    if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        Log.i("HTTP_NOT_FOUND 404", response.message());
                        return error(new BadRequestException());
                    }
                    return error(new RuntimeException(Objects.requireNonNull(response.errorBody()).toString()));
                }));
    }

    public Single<Void> unlockWorker(Long workerId) {
        return async(this.workerDao.unlockWorker(workerId)
                .flatMap(response -> {
                    if (response.isSuccessful()) {
                        return just(Objects.requireNonNull(response.body()));
                    }
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        Log.i("HTTP_UNAUTHORIZED 401", response.message());
                        return error((new UnauthorizedException()));
                    }
                    if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        Log.i("HTTP_NOT_FOUND 404", response.message());
                        return error(new BadRequestException());
                    }
                    return error(new RuntimeException(Objects.requireNonNull(response.errorBody()).toString()));
                }));
    }
}
