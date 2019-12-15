package com.project.courierapp.view.holders.holders_manager;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.project.courierapp.R;
import com.project.courierapp.databinding.DeliveryPointHolderBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.response.Response;
import com.project.courierapp.view.holders.BaseHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import icepick.State;


public class HolderDeliveryPoint extends BaseHolder {

    @State(ABundler.class)
    DeliveryPointResponse deliveryPointDto = new DeliveryPointResponse();

    public HolderDeliveryPoint(DeliveryPointHolderBinding deliveryPointHolderBinding) {
        super(deliveryPointHolderBinding.getRoot());
        List<Integer> idsList = Arrays.asList(
                R.id.address_content,
                R.id.longitude_content,
                R.id.latitude_content);
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
        Objects.requireNonNull((TextView) mapTextView.get(R.id.finished_roads_content))
                .setText("Lat: " + deliveryPointResponse.getLatitude());
    }

}
