package com.project.courierapp.model.dtos.response;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class WorkerResponse implements Response {

    @SerializedName("workerId")
    Long workerId;

    @SerializedName("login")
    String login;

    @SerializedName("status")
    String status;

    @SerializedName("finishedRoad")
    Long finishedRoad;

    @Override
    public String toString() {
        return login;
    }
}

