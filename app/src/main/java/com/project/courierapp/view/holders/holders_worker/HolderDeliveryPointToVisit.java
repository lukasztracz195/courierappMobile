package com.project.courierapp.view.holders.holders_worker;

import android.annotation.SuppressLint;
import android.widget.Button;
import android.widget.TextView;

import com.project.courierapp.R;
import com.project.courierapp.databinding.DeliveryPointToVisitItemBinding;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.response.Response;
import com.project.courierapp.view.holders.BaseHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HolderDeliveryPointToVisit extends BaseHolder {

    public HolderDeliveryPointToVisit(DeliveryPointToVisitItemBinding deliveryPointHolderBinding) {
        super(deliveryPointHolderBinding.getRoot());
        List<Integer> idsList = Arrays.asList(
                R.id.address_content,
                R.id.longitude_content,
                R.id.latitude_content,
                R.id.visite_bt);
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
        DeliveryPointResponse deliveryPointResponse = (DeliveryPointResponse) super.dataObject;
        Objects.requireNonNull((TextView) mapTextView.get(R.id.address_content))
                .setText(deliveryPointResponse.getAddress());
        Objects.requireNonNull((TextView) mapTextView.get(R.id.longitude_content))
                .setText("Lng: " + deliveryPointResponse.getLongitude());
        Objects.requireNonNull((TextView) mapTextView.get(R.id.latitude_content))
                .setText("Lat: " + deliveryPointResponse.getLatitude());

        Button visitButton = (Button) mapTextView.get(R.id.visite_bt);
        if(visitButton != null) {
            if (deliveryPointResponse.isVisited()) {
                visitButton.setEnabled(false);
                visitButton.setText(R.string.visited_delivery_point_bt);
            } else {
                visitButton.setEnabled(true);
                visitButton.setText(R.string.visit_delivery_point_bt);
            }
        }
    }
}
