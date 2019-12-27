package com.project.courierapp.model.dtos.response;

import com.google.gson.annotations.SerializedName;
import com.project.courierapp.model.enums.RoadState;

import org.joda.time.LocalDateTime;
import org.parceler.Parcel;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Parcel
@Getter
public class RoadResponse implements Response {

    @SerializedName("roadId")
      Long roadId;

    @SerializedName("worker")
      String worker;

    @SerializedName("deliveryPoints")
      List<DeliveryPointResponse> deliveryPoints;

    @SerializedName("state")
     RoadState state;

    @SerializedName("expectedTime")
     String expectedTime;

    @SerializedName("startedTime")
     LocalDateTime startedTime;

    @SerializedName("finishedTime")
      LocalDateTime finishedTime;

    @SerializedName("encodedPath")
      String encodedPath;
}
