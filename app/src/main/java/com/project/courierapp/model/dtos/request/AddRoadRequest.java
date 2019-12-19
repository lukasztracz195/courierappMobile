package com.project.courierapp.model.dtos.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AddRoadRequest implements Request {

    @SerializedName("workerId")
    Long workerId;

    @SerializedName("deliveryPoints")
    List<Long> deliveryPointsIds;
}
