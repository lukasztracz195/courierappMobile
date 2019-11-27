package com.project.sopmmobileapp.model.dtos.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class BaseResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("responseMesage")
    private String responseMessage;
}