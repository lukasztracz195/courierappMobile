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
public class TrackingPointsResponse implements Response, Comparable<TrackingPointsResponse> {

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

    @Override
    public int compareTo(TrackingPointsResponse trackingPointResponse) {
        if (this.getVisitTime().isBefore(trackingPointResponse.getVisitTime())) {
            return -1;
        }
        return 1;
    }
}
