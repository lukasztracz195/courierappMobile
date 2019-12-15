package com.project.courierapp.model.daos;

import com.project.courierapp.model.dtos.request.AddDeliveryPointRequest;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;

import java.util.List;

import javax.inject.Named;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface DeliveryPointsDao {

    String DELIVERY_POINTS_PATH = "/deliveryPoints";

    String DELIVERY_POINT_ID = "deliveryPointId";
    String ROAD_ID = "roadId";
    String DELIVERY_POINT_ID_PATH_VARIABLE = "{" + DELIVERY_POINT_ID + "}";
    String ROAD_ID_PATH_VARIABLE = "{" + ROAD_ID + "}";

    String ADD_DELIVERY_POINT = DELIVERY_POINTS_PATH;
    String ADD_DELIVERY_POINTS_PATH = DELIVERY_POINTS_PATH + "/addDeliveryPoints";
    String EDIT_ADDRESS_PATH = DELIVERY_POINTS_PATH + "/" + DELIVERY_POINT_ID_PATH_VARIABLE +
            "/address";
    String GET_BY_ID_PATH = DELIVERY_POINTS_PATH + "/" + DELIVERY_POINT_ID_PATH_VARIABLE;
    String GET_ALL_PATH = DELIVERY_POINTS_PATH;
    String GET_BY_ROAD_ID_PATH = DELIVERY_POINTS_PATH + "/road/" + ROAD_ID_PATH_VARIABLE;
    String DELETE_BY_ID_PATH = DELIVERY_POINTS_PATH + "/" + DELIVERY_POINT_ID_PATH_VARIABLE;
    String VISIT_PATH = DELIVERY_POINTS_PATH + "/" + DELIVERY_POINT_ID_PATH_VARIABLE + "/visit";

    @POST(ADD_DELIVERY_POINT)
    Single<Response<DeliveryPointResponse>> addDeliveryPoint(
            @Body AddDeliveryPointRequest addDeliveryPointRequest);


    @POST(ADD_DELIVERY_POINTS_PATH)
    Single<Response<List<Long>>> addDeliveryPoint(
            @Body List<AddDeliveryPointRequest> addDeliveryPointRequestList);

    @PUT(EDIT_ADDRESS_PATH)
    Single<Response<DeliveryPointResponse>> editDeliveryPoint(
            @Named(DELIVERY_POINT_ID) Long deliveryPointId,
            @Body AddDeliveryPointRequest editDeliveryPointRequest);


    @GET(GET_BY_ID_PATH)
    Single<Response<DeliveryPointResponse>> getDeliveryPointById(
            @Named(DELIVERY_POINT_ID) Long deliveryPointId);

    @GET(GET_ALL_PATH)
    Single<Response<List<DeliveryPointResponse>>> getAllDeliveryPoints();

    @GET(GET_BY_ROAD_ID_PATH)
    Single<Response<List<DeliveryPointResponse>>> getDeliveryPointsByRoadId(
            @Named(ROAD_ID) Long roadId);

    @DELETE(DELETE_BY_ID_PATH)
    Single<Response<Boolean>> deleteDeliveryPointById(
            @Named(DELIVERY_POINT_ID) Long deliveryPointId);
}
