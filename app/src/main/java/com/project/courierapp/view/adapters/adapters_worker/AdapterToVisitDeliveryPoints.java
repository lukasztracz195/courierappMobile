package com.project.courierapp.view.adapters.adapters_worker;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.DeliveryPointToVisitItemBinding;
import com.project.courierapp.model.di.clients.DeliveryPointsClient;
import com.project.courierapp.model.dtos.request.LocationRequest;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.service.LocationService;
import com.project.courierapp.model.store.LastStartedRoadStore;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.adapters.BaseAdapter;
import com.project.courierapp.view.holders.BaseHolder;
import com.project.courierapp.view.holders.holders_worker.HolderDeliveryPointToVisit;
import com.project.courierapp.view.toasts.ToastFactory;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import io.reactivex.disposables.Disposable;
import lombok.Getter;

@Getter
public class AdapterToVisitDeliveryPoints extends BaseAdapter {

    @BindView(R.id.visite_bt)
    Button visitButton;

    Button finishRoadButton;

    @Inject
    DeliveryPointsClient deliveryPointsClient;

    public AdapterToVisitDeliveryPoints(Context context, Bundle savedInstanceState, List<DeliveryPointResponse> deliveryPointResponseList, Button finishRoadButton) {
        super(context, savedInstanceState);
        CourierApplication.getClientsComponent().inject(this);
        this.finishRoadButton = finishRoadButton;
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        if(!responses.equals(deliveryPointResponseList)){
            responses = deliveryPointResponseList;
        }
        if(responses.isEmpty()) {
            downloadData();
        }
        responses = deliveryPointResponseList.stream().sorted(Comparator
                .comparingInt(DeliveryPointResponse::getOrder)).collect(Collectors.toList());

        if (allPointsVisited()) {
            finishRoadButton.setEnabled(true);
        } else {
            finishRoadButton.setEnabled(false);
        }
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        super.onCreateViewHolder(viewGroup, viewType);
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        DeliveryPointToVisitItemBinding deliveryPointToVisitItemBinding = DataBindingUtil
                .inflate(layoutInflater,
                        R.layout.delivery_point_to_visit_item, viewGroup, false);
        view = deliveryPointToVisitItemBinding.getRoot();
        ButterKnife.bind(this, view);
        return new HolderDeliveryPointToVisit(deliveryPointToVisitItemBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull BaseHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        DeliveryPointResponse deliveryPointResponse = (DeliveryPointResponse) responses.get(position);
        setActionOnVisitDeliveryPoint(deliveryPointResponse, position);
        holder.setFields((DeliveryPointResponse) responses.get(position));
    }

    @Override
    public void downloadData() {
        super.downloadData();
        Long lastStartedRoadId = LastStartedRoadStore.getLastStartedRoadId();
        if (lastStartedRoadId > 0) {
            Disposable disposable = deliveryPointsClient.getDeliveryPointsByRoadId(lastStartedRoadId).subscribe(
                    this::updateAndSortData, e -> Log.e(AdaptersTags.AdapterWorkersListItem,
                            e.getMessage(), e));
            compositeDisposable.add(disposable);
        }
    }

    private void setActionOnVisitDeliveryPoint(DeliveryPointResponse deliveryPointResponse, int position) {
        visitButton.setOnClickListener(view -> {
            if(LocationService.instance != null) {
                LocationService locationService = LocationService.instance;
                Location location = locationService.getLocation();
                if(location != null) {
                    if (!((DeliveryPointResponse) responses.get(position)).isVisited()) {
                        Disposable disposable = deliveryPointsClient.visitDeliveryPoint(
                                deliveryPointResponse.getPointId(), LocationRequest.builder()
                                        .longitude(location.getLongitude())
                                        .latitude(location.getLatitude())
                                        .build()).subscribe(responseMessage -> {
                            ((DeliveryPointResponse) responses.get(position)).setVisited(true);
                            super.updateData(responses);
                        }, (Throwable e) -> {
                            ToastFactory.createToast(context, e.getMessage());
                        });
                    }
                }
            }
        });

    }

    public boolean allPointsVisited() {
        List<DeliveryPointResponse> deliveryPointResponseList = responses;
        if (deliveryPointResponseList != null) {
            return deliveryPointResponseList.stream().allMatch(DeliveryPointResponse::isVisited);
        }
        return false;
    }

    public void updateAndSortData(List<DeliveryPointResponse> deliveryPointResponseList) {
        List<DeliveryPointResponse> list = deliveryPointResponseList.stream()
                .sorted(Comparator.comparingInt(DeliveryPointResponse::getOrder))
                .collect(Collectors.toList());
        super.updateData(list);
    }

    public void onSaveInstanceState(Bundle outState) {
        Optional<Bundle> bundleOptional = Optional.ofNullable(outState);
        if(bundleOptional.isPresent()) {
            Icepick.saveInstanceState(this, outState);
        }
    }
}
