package com.project.courierapp.model.dtos.request;

import com.google.gson.annotations.SerializedName;

import java.time.Duration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddDeliveryPointRequest implements Request{

    @SerializedName("address")
    String address;

    @SerializedName("expectedSpendTime")
    Duration expectedSpendTime;
}
