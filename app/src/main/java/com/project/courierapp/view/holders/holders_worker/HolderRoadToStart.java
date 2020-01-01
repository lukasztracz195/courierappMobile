package com.project.courierapp.view.holders.holders_worker;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.project.courierapp.R;
import com.project.courierapp.databinding.RoadToStartItemBinding;
import com.project.courierapp.model.dtos.response.Response;
import com.project.courierapp.model.dtos.response.RoadResponse;
import com.project.courierapp.view.holders.BaseHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HolderRoadToStart extends BaseHolder {

    public HolderRoadToStart(RoadToStartItemBinding roadToStartItemBinding) {
        super(roadToStartItemBinding.getRoot());
        List<Integer> idsList = Arrays.asList(
                R.id.worker_points_to_visit_content,
                R.id.worker_road_status_content);
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
        Objects.requireNonNull((TextView) mapTextView.get(R.id.worker_road_status_content))
                .setText(roadResponse.getState().toString());
    }
}
