package com.project.courierapp.model.dtos.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AddTrackingPointRequest implements Request{

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;
}
