package com.project.courierapp.model.dtos.response;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

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
@Parcel
public class DeliveryPointResponse implements Response{

    @SerializedName("pointId")
    long pointId;

    @SerializedName("address")
    String address;

    @SerializedName("visited")
    boolean visited;

    @SerializedName("expectedSpendTime")
    String expectedSpendTime;

    @SerializedName("latitude")
    double latitude;

    @SerializedName("longitude")
    double longitude;

    @SerializedName("order")
    Integer order;
}
