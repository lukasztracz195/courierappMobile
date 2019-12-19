package com.project.courierapp.model.dtos.response;

import com.google.gson.annotations.SerializedName;
import com.project.courierapp.model.enums.RoadState;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class RoadResponse implements Response {

    @SerializedName("roadId")
    private  Long roadId;

    @SerializedName("workerId")
    private  long workerId;

    @SerializedName("deliveryPoints")
    private  List<DeliveryPointResponse> deliveryPoints;

    @SerializedName("state")
    private RoadState state;

    @SerializedName("expectedTime")
    private  Duration expectedTime;

    @SerializedName("startedTime")
    private  LocalDateTime startedTime;

    @SerializedName("finishedTime")
    private  LocalDateTime finishedTime;

    @SerializedName("encodedPath")
    private  String encodedPath;
}
