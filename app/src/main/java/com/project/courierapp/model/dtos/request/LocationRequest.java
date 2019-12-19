package com.project.courierapp.model.dtos.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class LocationRequest implements Request {

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;
}
