package com.project.courierapp.model.dtos.response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerResponse implements Response{

    @SerializedName("workerId")
    private Long workerId;

    @SerializedName("login")
    private String login;

    @SerializedName("status")
    private String status;

    @SerializedName("finishedRoad")
    private Long finishedRoad;

    @Override
    public String toString(){
        return login;
    }
}

