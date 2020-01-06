package com.project.courierapp.view.holders.holders_worker;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.project.courierapp.R;
import com.project.courierapp.databinding.FinishedRoadItemBinding;
import com.project.courierapp.model.calculator.DistanceCalculator;
import com.project.courierapp.model.calculator.TimeCalculator;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.response.Response;
import com.project.courierapp.model.dtos.response.RoadResponse;
import com.project.courierapp.view.holders.BaseHolder;

import org.joda.time.Instant;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HolderFinishedRoad extends BaseHolder {

    private static int MILISS_IN_HOUR = 3_600_000;

    public HolderFinishedRoad(FinishedRoadItemBinding finishedRoadItemBinding) {
        super(finishedRoadItemBinding.getRoot());
        List<Integer> idsList = Arrays.asList(
                R.id.worker_points_visited_content,
                R.id.worker_road_distance_content,
                R.id.worker_road_time_traveled_content,
                R.id.worker_road_performance_opinion_content);
        initTextViews(idsList);
    }

    @Override
    public void initTextViews(List<Integer> ids) {
        super.initTextViews(ids);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void setFields(Response object) {
        super.setFields(object);
        RoadResponse roadResponse = (RoadResponse) super.dataObject;
        Objects.requireNonNull((TextView) mapTextView.get(R.id.worker_points_to_visit_content))
                .setText(roadResponse.getDeliveryPoints().size());
        double distanceInKilometers = DistanceCalculator
                .caluculateDistanceFromListDeliveryPoints(roadResponse.getDeliveryPoints().stream()

                .sorted(Comparator.comparingInt(DeliveryPointResponse::getOrder))
                .collect(Collectors.toList()));
        Objects.requireNonNull((TextView) mapTextView.get(R.id.worker_road_distance_content))
                .setText(String.valueOf(distanceInKilometers));
        double traveledInHours = TimeCalculator.calculatedHoursBetween(
                roadResponse.getStartedTime(),
                roadResponse.getFinishedTime());
        Objects.requireNonNull((TextView) mapTextView.get(R.id.worker_road_time_traveled_content))
                .setText(String.valueOf(traveledInHours));

        double expectedHours = Instant.parse(roadResponse.getExpectedTime())
                .getMillis() / MILISS_IN_HOUR;
        double difference = expectedHours - traveledInHours;
        if (difference < 0) {
            Objects.requireNonNull((TextView) mapTextView.get(
                    R.id.worker_road_performance_opinion_content))
                    .setText("Slower by expected by " + traveledInHours + " h");
        } else {
            Objects.requireNonNull((TextView) mapTextView.get(
                    R.id.worker_road_performance_opinion_content))
                    .setText("Fastest by expected by " + traveledInHours + " h");
        }
    }
}
