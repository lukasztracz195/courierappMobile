package com.project.courierapp.view.holders.holders_manager;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.project.courierapp.R;
import com.project.courierapp.model.converters.LocalDateTimeFormatter;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.response.Response;
import com.project.courierapp.model.dtos.response.RoadResponse;
import com.project.courierapp.view.holders.BaseHolder;
import com.project.courierapp.view.holders.IdsListBuilder;

import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HolderRoad extends BaseHolder {
    public HolderRoad(@NonNull View itemView) {
        super(itemView);
        initTextViews(new IdsListBuilder()
                .appendTextViewId(R.id.assigned_worker_content)
                .appendTextViewId(R.id.points_road_content)
                .appendTextViewId(R.id.start_time_content)
                .appendTextViewId(R.id.visited_points_content)
                .appendTextViewId(R.id.finish_time_content)
                .build());
    }

    @Override
    public void initTextViews(List<Integer> ids) {
        super.initTextViews(ids);
    }


    @Override
    public void setFields(Response object) {
        super.setFields(object);
        RoadResponse roadResponse = (RoadResponse) super.dataObject;
        Objects.requireNonNull((TextView) mapTextView.get(R.id.assigned_worker_content))
                .setText(roadResponse.getWorker());
        Objects.requireNonNull((TextView) mapTextView.get(R.id.points_road_content))
                .setText(String.valueOf(roadResponse.getDeliveryPoints().size()));
        Objects.requireNonNull((TextView) mapTextView.get(R.id.start_time_content))
                .setText(String.valueOf(
                        LocalDateTimeFormatter.format(roadResponse.getStartedTime())));
        Objects.requireNonNull((TextView) mapTextView.get(R.id.visited_points_content))
                .setText(String.valueOf(roadResponse.getDeliveryPoints().stream().filter(
                        DeliveryPointResponse::isVisited)
                        .count()));
        Optional<LocalDateTime> optionalLocalDateTime = Optional.ofNullable(roadResponse
                .getFinishedTime());
        optionalLocalDateTime.ifPresent(localDateTime ->
                Objects.requireNonNull((TextView) mapTextView.get(R.id.finish_time_content))
                .setText(String.valueOf(LocalDateTimeFormatter.format(localDateTime))));
    }
}
