package com.project.courierapp.view.adapters.adapters_manager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.project.courierapp.R;
import com.project.courierapp.databinding.DeliveryPointHolderBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.dtos.transfer.DeliveryPointDto;
import com.project.courierapp.view.adapters.BaseAdapter;
import com.project.courierapp.view.holders.BaseHolder;
import com.project.courierapp.view.holders.holders_manager.HolderDeliveryPoint;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AdapterDeliveryPoints extends BaseAdapter {


    @State(ABundler.class)
    List<DeliveryPointDto> deliveryPointDtoList;

    private Bundle savedInstanceState;

    @BindView(R.id.save_bt)
    Button saveButton;

    @BindView(R.id.delete_bt)
    Button deleteButton;

    public AdapterDeliveryPoints(Context context, List<DeliveryPointDto> deliveryPointDtos, Bundle savedInstanceState) {
        super(context);
        this.deliveryPointDtoList = deliveryPointDtos;
        this.savedInstanceState = savedInstanceState;
    }

    @NonNull
    @Override
    public HolderDeliveryPoint onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        super.onCreateViewHolder(viewGroup, viewType);
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        DeliveryPointHolderBinding deliveryPointHolderBinding = DataBindingUtil
                .inflate(layoutInflater,
                        R.layout.delivery_point_holder, viewGroup, false);
        this.view = deliveryPointHolderBinding.getRoot();
        ButterKnife.bind(this, view);
        return new HolderDeliveryPoint(deliveryPointHolderBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull BaseHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        DeliveryPointDto deliveryPointDto = deliveryPointDtoList.get(position);
        ((HolderDeliveryPoint) holder).bind(deliveryPointDto);
//        setActionOnSaveDeliveryPoint(deliveryPointDto);
        setActionOnDeleteDeliveryPoint(position);
    }

    public void addPoint() {
        deliveryPointDtoList.add(new DeliveryPointDto());
        super.notifyDataSetChanged();
    }

    public void removePoint() {
        if (!deliveryPointDtoList.isEmpty()) {
            deliveryPointDtoList.remove(deliveryPointDtoList.size() - 1);
            super.notifyDataSetChanged();
        }
    }

    public void setActionOnSaveDeliveryPoint(DeliveryPointDto deliveryPointDto) {
//        saveButton.setOnClickListener(view -> {
//            //TODO Validation and send data to server
//        });
    }

    public void setActionOnDeleteDeliveryPoint(int position ) {
        deleteButton.setOnClickListener(view -> {
            if(position >= 0 && position < deliveryPointDtoList.size()){
                deliveryPointDtoList.remove(position);
                super.notifyItemRemoved(position);
            }else{
                if(position != deliveryPointDtoList.size() && !deliveryPointDtoList.isEmpty()){
                    deliveryPointDtoList.remove(0);
                    super.notifyItemRemoved(0);
                }
            }

        });
    }


    @OnClick(R.id.delete_bt)
    public void delete() {

    }

    public void removePoint(int position) {
        if (!deliveryPointDtoList.isEmpty()) {
            deliveryPointDtoList.remove(position);
        }

    }

    @Override
    public int getItemCount() {
        return deliveryPointDtoList.size();
    }

    public List<DeliveryPointDto> getDeliveryPointDtoList() {
        return deliveryPointDtoList;
    }


    public void onSaveInstanceState(@NotNull Bundle outState) {
        Icepick.saveInstanceState(this, outState);
    }

}
