package com.project.courierapp.view.holders.holders_manager;

import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.project.courierapp.R;
import com.project.courierapp.databinding.DeliveryPointHolderBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.dtos.transfer.DeliveryPointDto;
import com.project.courierapp.model.watchers.WatcherEditText;
import com.project.courierapp.view.holders.BaseHolder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import icepick.State;


public class HolderDeliveryPoint extends BaseHolder {

    private final DeliveryPointHolderBinding deliveryPointHolderBinding;
    private Map<Integer, String> mapText = new HashMap<>();
    private TextView errorTextView;
    @State(ABundler.class)
    DeliveryPointDto deliveryPointDto = new DeliveryPointDto();

    public HolderDeliveryPoint(DeliveryPointHolderBinding deliveryPointHolderBinding) {
        super(deliveryPointHolderBinding.getRoot());
        this.deliveryPointHolderBinding = deliveryPointHolderBinding;
        List<Integer> idsList = Arrays.asList(
                R.id.address_content,
                R.id.postal_code_content,
                R.id.city_content,
                R.id.country_content,
                R.id.expected_time_content);
        initTextViews(idsList);
        errorTextView = deliveryPointHolderBinding.getRoot().findViewById(R.id.validation_label);
        List<String> contentDeliveryPointDto = Arrays.asList(
                deliveryPointDto.getAddress(),
                deliveryPointDto.getPostalCode(),
                deliveryPointDto.getCity(),
                deliveryPointDto.getCountry(),
                deliveryPointDto.getExpendedTime());

        for(int i=0; i<contentDeliveryPointDto.size(); i++){
            mapText.put(idsList.get(i),contentDeliveryPointDto.get(i));
        }
        for (Map.Entry<Integer, ? super TextView> entry : mapTextView.entrySet())
            ((TextInputEditText) entry.getValue()).addTextChangedListener(
                    new WatcherEditText(deliveryPointDto, mapText.get(entry.getKey()), errorTextView));
    }

    public void bind(DeliveryPointDto deliveryPointDto){
        deliveryPointHolderBinding.setDeliveryPointDto(deliveryPointDto);
        deliveryPointHolderBinding.executePendingBindings();
    }

    public DeliveryPointDto getDeliveryPointDto() {
        return deliveryPointDto;
    }
}
