package com.project.courierapp.model.dtos.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class BaseResponse implements Response {

    @SerializedName("status")
    private boolean status;

    @SerializedName("responseMesage")
    private String responseMessage;
}