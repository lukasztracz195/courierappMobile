package com.project.courierapp.model.daos;

import com.project.courierapp.model.dtos.request.AddTrackingPointRequest;
import com.project.courierapp.model.dtos.response.TrackingPointsResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TrackingPointsDao {
    String TRACKING_POINT_BASE_PATH = "/trackingPoints";

    String ROAD_ID = "roadId";
    String ROAD_ID_PATH_VARIABLE = "{" + ROAD_ID + "}";

    String ADD_PATH = TRACKING_POINT_BASE_PATH + "/" + ROAD_ID_PATH_VARIABLE;
    String GET_BY_ROAD_ID_PATH = TRACKING_POINT_BASE_PATH + "/road/" + ROAD_ID_PATH_VARIABLE;
    String GET_BY_STARTED_ROAD_BY_LOGGED_WORKER = TRACKING_POINT_BASE_PATH + "/me/trace";

    @POST(ADD_PATH)
    Single<Response<TrackingPointsResponse>> addTrackingPointResponse(@Path(ROAD_ID) Long roadId,
                                                                      @Body AddTrackingPointRequest addTrackingPointRequest);

    @GET(GET_BY_ROAD_ID_PATH)
    Single<Response<List<TrackingPointsResponse>>> getByRoadId(
            @Path(ROAD_ID) Long roadId);

    @GET(GET_BY_STARTED_ROAD_BY_LOGGED_WORKER)
    Single<Response<List<TrackingPointsResponse>>> getTraceStartedRoadByLoggedWorker();
}
