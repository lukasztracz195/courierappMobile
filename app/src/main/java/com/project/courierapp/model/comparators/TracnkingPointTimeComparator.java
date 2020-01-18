package com.project.courierapp.model.comparators;

import com.project.courierapp.model.dtos.response.TrackingPointsResponse;

import java.util.Comparator;

public class TracnkingPointTimeComparator implements Comparator<TrackingPointsResponse> {

    @Override
    public int compare(TrackingPointsResponse o1, TrackingPointsResponse o2) {
        if(o1 != null && o2 != null){
            if(o1.getVisitTime().isAfter(o2.getVisitTime())){
                return -1;
            }
            return 1;
        }
        return 0;
    }
}
