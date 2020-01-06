package com.project.courierapp.view.adapters.adapters_worker;

import android.content.Context;
import android.content.Intent;
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
import com.project.courierapp.databinding.RoadToStartItemBinding;
import com.project.courierapp.model.di.clients.RoadClient;
import com.project.courierapp.model.dtos.request.LocationRequest;
import com.project.courierapp.model.dtos.response.RoadResponse;
import com.project.courierapp.model.service.LocationService;
import com.project.courierapp.model.store.LastStartedRoadStore;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.adapters.Adapter;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.adapters.BaseAdapter;
import com.project.courierapp.view.fragments.BaseFragmentTags;
import com.project.courierapp.view.fragments.base_layer.WorkerBaseFragment;
import com.project.courierapp.view.holders.BaseHolder;
import com.project.courierapp.view.holders.holders_worker.HolderRoadToStart;
import com.project.courierapp.view.toasts.ToastFactory;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class AdapterRoadsToStart extends BaseAdapter implements Adapter {
  
    @BindView(R.id.start_road_bt)
    Button startRoadButton;

    @Inject
    RoadClient roadClient;

    public AdapterRoadsToStart(Context context, Bundle savedInstanceState) {
        super(context, savedInstanceState);
        CourierApplication.getClientsComponent().inject(this);
        if(LocationService.instance == null) {
            Intent intent = new Intent(MainActivity.instance, LocationService.class);
            context.startService(intent);
        }
        downloadData();
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        super.onCreateViewHolder(viewGroup, viewType);
        if(responses == null){
            downloadData();
        }
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        RoadToStartItemBinding roadToStartItemBinding = DataBindingUtil
                .inflate(layoutInflater,
                        R.layout.road_to_start_item, viewGroup, false);
        view = roadToStartItemBinding.getRoot();
        ButterKnife.bind(this, view);
        return new HolderRoadToStart(roadToStartItemBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull BaseHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(responses!= null && responses.size() > position) {
            RoadResponse roadResponse = (RoadResponse) responses.get(position);
            setActionOnStartRoad(roadResponse, position);
            holder.setFields((RoadResponse) responses.get(position));
        }
    }

    @Override
    public void downloadData() {
        super.downloadData();
        Disposable disposable = roadClient.getAllPlannedRoadForLoggedWorker().subscribe(
                this::updateData, e -> Log.e(AdaptersTags.AdapterWorkersListItem,
                        e.getMessage(), e));
        compositeDisposable.add(disposable);
    }


    private void setActionOnStartRoad(RoadResponse roadResponse, int position) {
        startRoadButton.setOnClickListener(view -> {
            LastStartedRoadStore.saveRoadId(roadResponse.getRoadId());
            LocationService locationService = LocationService.instance;
            while(locationService == null) {
                locationService = LocationService.instance;
            }
                    Location location = locationService.getLocation();
                    while (location == null) {
                        location = locationService.getLocation();
                    }
                        Disposable disposable = roadClient.startRoad(roadResponse.getRoadId(),
                                LocationRequest.builder()
                                        .latitude(location.getLatitude())
                                        .longitude(location.getLongitude())
                                        .build()
                        ).subscribe(responseMessage -> {
                            RoadResponse roadResponseFromAdapter = ((RoadResponse) responses.get(position));
                            roadResponseFromAdapter = RoadResponse.of(responseMessage);
                            super.updateData(responses);
                            ((MainActivity) context).putFragment(new WorkerBaseFragment(true), BaseFragmentTags.WorkerBaseFragment);
                        }, (Throwable e) -> {
                            ToastFactory.createToast(context, e.getMessage());
                        });
                    locationService.setSendingTrackingPointsIsActivated(true);

        });

    }
}
