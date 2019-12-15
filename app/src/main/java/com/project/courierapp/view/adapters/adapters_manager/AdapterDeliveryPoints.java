package com.project.courierapp.view.adapters.adapters_manager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.DeliveryPointHolderBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.di.clients.DeliveryPointsClient;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.transfer.DeliveryPointDto;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.adapters.BaseAdapter;
import com.project.courierapp.view.fragments.manager_layer.ManagerFragmentTags;
import com.project.courierapp.view.fragments.manager_layer.functional.CreateDeliveryPointFragment;
import com.project.courierapp.view.holders.BaseHolder;
import com.project.courierapp.view.holders.holders_manager.HolderDeliveryPoint;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AdapterDeliveryPoints extends BaseAdapter {


    @BindView(R.id.validation_label)
    TextView validationMessage;

    @BindView(R.id.edit_bt)
    Button editButton;

    @BindView(R.id.delete_bt)
    Button deleteButton;

    @Inject
    DeliveryPointsClient deliveryPointsClient;

    @State(ABundler.class)
    List<DeliveryPointResponse> deliveryPointResponseList;

    private Bundle savedInstanceState;

    public AdapterDeliveryPoints(Context context, List<DeliveryPointResponse> deliveryPointDtos, Bundle savedInstanceState) {
        super(context);
        this.deliveryPointResponseList = deliveryPointDtos;
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
        CourierApplication.getClientsComponent().inject(this);
        return new HolderDeliveryPoint(deliveryPointHolderBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull BaseHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        DeliveryPointResponse deliveryPointDto = deliveryPointResponseList.get(position);
        setActionOnEditDeliveryPoint(deliveryPointDto);
        setActionOnDeleteDeliveryPoint(position);
    }

    public void addPoint() {
        deliveryPointResponseList.add(new DeliveryPointResponse());
        super.notifyDataSetChanged();
    }

    public void removePoint() {
        if (!deliveryPointResponseList.isEmpty()) {
            deliveryPointResponseList.remove(deliveryPointResponseList.size() - 1);
            super.notifyDataSetChanged();
        }
    }

    public void setActionOnEditDeliveryPoint(DeliveryPointResponse deliveryPointDto) {
        editButton.setOnClickListener(view -> {
            CreateDeliveryPointFragment createDeliveryPointFragment =
                    new CreateDeliveryPointFragment(deliveryPointResponseList);
            createDeliveryPointFragment.setDeliveryPointDto(new DeliveryPointDto(deliveryPointDto));

            ((MainActivity) Objects.requireNonNull(CourierApplication.getContext()))
                    .putFragment(createDeliveryPointFragment,
                            ManagerFragmentTags.CreateRoadFragment);
        });
    }


    public void setActionOnDeleteDeliveryPoint(int position) {
        deleteButton.setOnClickListener(view -> {
            if (position >= 0 && position < deliveryPointResponseList.size()) {
                deliveryPointResponseList.remove(position);
                super.notifyItemRemoved(position);
            } else {
                if (position != deliveryPointResponseList.size() && !deliveryPointResponseList.isEmpty()) {
                    deliveryPointResponseList.remove(0);
                    super.notifyItemRemoved(0);
                }
            }
        });
    }


    @OnClick(R.id.delete_bt)
    public void delete() {

    }

    public void removePoint(int position) {
        if (!deliveryPointResponseList.isEmpty()) {
            deliveryPointResponseList.remove(position);
        }

    }

    @Override
    public int getItemCount() {
        return deliveryPointResponseList.size();
    }

    public List<DeliveryPointResponse> getDeliveryPointResponseList() {
        return deliveryPointResponseList;
    }


    public void onSaveInstanceState(@NotNull Bundle outState) {
        Icepick.saveInstanceState(this, outState);
    }

}
