package com.project.courierapp.model.dtos.response;

import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDateTime;
import org.parceler.Parcel;

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
public class TrackingPointsResponse implements Response {

    @SerializedName("trackingPointId")
    Long trackingPointId;

    @SerializedName("roadId")
    Long roadId;

    @SerializedName("visitTime")
    LocalDateTime visitTime;

    @SerializedName("longitude")
    double longitude;

    @SerializedName("latitude")
    double latitude;
}
