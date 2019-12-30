package com.project.courierapp.model.daos;

import com.project.courierapp.model.dtos.response.IsBusyResponse;
import com.project.courierapp.model.dtos.response.WorkerResponse;

import java.util.List;

import javax.inject.Named;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface WorkerDao {

    String WORKER_ID = "workerId";
    String LOGIN = "login";

    String LOGIN_PATH_VARIABLE = "{" + LOGIN + "}";
    String WORKER_ID_PATH_VARIABLE = "{" + WORKER_ID + "}";

    String GET_WORKERS_PATH = "/getWorkers";
    String LOCK_WORKER_PATH = "lock/user/" + WORKER_ID_PATH_VARIABLE;
    String UNLOCK_WORKER_PATH = "/unlockUser/" + WORKER_ID_PATH_VARIABLE;
    String RESET_PASSWORD_PATH = "/resetPassword/" + LOGIN_PATH_VARIABLE;
    String IS_BUSY_PATH = "isBusy";


    @GET(GET_WORKERS_PATH)
    Single<Response<List<WorkerResponse>>> getWorkers();

    @PUT(LOCK_WORKER_PATH)
    Single<Response<Void>> lockWorker(@Named(WORKER_ID) Long workerId);

    @PUT(UNLOCK_WORKER_PATH)
    Single<Response<Void>> unlockWorker(@Named(WORKER_ID) Long workerId);

    @POST(RESET_PASSWORD_PATH)
    Single<Response<Void>> resetPassword(@Named(LOGIN) String login);

    @GET(IS_BUSY_PATH)
    Single<Response<IsBusyResponse>> isBusy();

}
