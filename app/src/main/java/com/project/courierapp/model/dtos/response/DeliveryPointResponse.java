package com.project.courierapp.model.dtos.response;

import com.google.gson.annotations.SerializedName;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DeliveryPointResponse {

    @SerializedName("pointId")
    long pointId;

    @SerializedName("address")
    String address;

    @SerializedName("expectedSpendTime")
    Duration expectedSpendTime;

    @SerializedName("latitude")
    double latitude;

    @SerializedName("longitude")
    double longitude;

    @SerializedName("order")
    Integer order;
}
