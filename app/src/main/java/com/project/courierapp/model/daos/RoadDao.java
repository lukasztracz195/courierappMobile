package com.project.courierapp.model.daos;

import com.project.courierapp.model.dtos.request.AddRoadRequest;
import com.project.courierapp.model.dtos.request.LocationRequest;
import com.project.courierapp.model.dtos.response.RoadResponse;

import java.util.List;

import javax.inject.Named;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RoadDao {

    String ROAD_PATH = "/roads";

    String WORKER_ID = "workerId";
    String ROAD_ID = "roadId";

    String WORKER_ID_PATH_VARIABLE = "{" + WORKER_ID + "}";
    String ROAD_ID_PATH_VARIABLE = "{" + ROAD_ID + "}";

    String ADD_PATH = ROAD_PATH + "";
    String GET_BY_WORKER_ID_PATH = ROAD_PATH + "/worker/" + WORKER_ID_PATH_VARIABLE;

    String STARTED_TIME_ID_PATH = ROAD_PATH + ROAD_ID_PATH_VARIABLE + "/startedTime";
    String FINISHED_TIME_ID_PATH = ROAD_PATH + ROAD_ID_PATH_VARIABLE + "/finishedTime";

    String GET_PLANNED_ROADS_PATH = ROAD_PATH + "/plannedTime";
    String GET_PLANNED_ROADS_BY_WORKER_PATH = ROAD_PATH + GET_BY_WORKER_ID_PATH + "/plannedTime";

    String GET_STARTED_ROADS_PATH = ROAD_PATH + "/startedTime";
    String GET_STARTED_ROADS_BY_WORKER_PATH = ROAD_PATH + GET_BY_WORKER_ID_PATH + "/startedTime";

    String GET_FINISHED_ROADS_PATH = "/finishedTime";
    String GET_FINISHED_ROADS_BY_WORKER_PATH = ROAD_PATH + GET_BY_WORKER_ID_PATH + "/finishedTime";

    String GET_PLANNED_WORKER_ROADS_PATH = ROAD_PATH + "/me/planned";

    String DELETE_ROAD_PATH = ROAD_PATH + ROAD_ID_PATH_VARIABLE;

    @POST(ADD_PATH)
    Single<Response<RoadResponse>> add(@Body AddRoadRequest addRoadRequest);

    @PUT(STARTED_TIME_ID_PATH)
    Single<Response<RoadResponse>> startRoad(@Named(ROAD_ID) Long roadId,
                                             @Body LocationRequest locationRequest);

    @PUT(FINISHED_TIME_ID_PATH)
    Single<Response<RoadResponse>> finishRoad(@Named(ROAD_ID) Long roadId,
                                              @Body LocationRequest locationRequest);

    @GET(GET_BY_WORKER_ID_PATH)
    Single<Response<RoadResponse>> getRoadByWorkerId(@Named(WORKER_ID) Long workerId);

    @GET(GET_PLANNED_ROADS_PATH)
    Single<Response<List<RoadResponse>>> getAllPlannedRoads();

    @GET(GET_PLANNED_ROADS_BY_WORKER_PATH)
    Single<Response<List<RoadResponse>>> getPlannedRoadsByWorkerId(@Named(WORKER_ID)
                                                                              Long workerId);

    @GET(GET_STARTED_ROADS_PATH)
    Single<Response<List<RoadResponse>>> getAllStartedRoads();

    @GET(GET_STARTED_ROADS_BY_WORKER_PATH)
    Single<Response<List<RoadResponse>>> getStartedRoadsByWorkerId(@Named(WORKER_ID)
                                                                              Long workerId);

    @GET(GET_FINISHED_ROADS_PATH)
    Single<Response<List<RoadResponse>>> getAllFinishedRoads();

    @GET(GET_FINISHED_ROADS_BY_WORKER_PATH)
    Single<Response<List<RoadResponse>>> getFinishedRoadsByWorkerId(@Named(WORKER_ID)
                                                                           Long workerId);

    @GET(GET_PLANNED_WORKER_ROADS_PATH)
    Single<Response<List<RoadResponse>>> getAllPlannedRoadForLoggedWorker();


    @DELETE(DELETE_ROAD_PATH)
    Single<Response<Boolean>> deleteRoadById(@Named(ROAD_ID) Long roadId);
}
