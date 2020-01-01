package com.project.courierapp.view.adapters.adapters_worker;

import android.content.Context;
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
import com.project.courierapp.model.dtos.response.RoadResponse;
import com.project.courierapp.view.adapters.Adapter;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.adapters.BaseAdapter;
import com.project.courierapp.view.holders.BaseHolder;
import com.project.courierapp.view.holders.holders_worker.HolderRoadToStart;

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
        downloadData();
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        super.onCreateViewHolder(viewGroup, viewType);
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
        RoadResponse roadResponse = (RoadResponse) responses.get(position);
        setActionOnStartRoad(roadResponse, position);
        holder.setFields((RoadResponse) responses.get(position));
    }

    @Override
    public void downloadData() {
        super.downloadData();
        Disposable disposable = roadClient.getAllPlannedRoadForLoggedWorker().subscribe(
                this::updateData, e -> Log.e(AdaptersTags.AdapterWorkersListItem,
                        e.getMessage(), e));
        compositeDisposable.add(disposable);
    }

    private void setActionOnStartRoad(RoadResponse roadResponse, int position){
        startRoadButton.setOnClickListener(view -> {
//            LastStartedRoadStore.saveRoadId(roadResponse.getRoadId());
            //TODO START LOCATION SERVICE
            //TODO CHANGE FRAGMENT ON FRAGMENT WHEN WORKER IS BUSY

//                Disposable disposable = roadClient.startRoad(roadResponse.getRoadId(),
//                        LocationRequest.builder().build()
//                ).subscribe(responseMessage ->{
//                    ((RoadResponse) responses.get(position))
//                    super.updateData(responses);
//                }, (Throwable e) -> {
//                    ToastFactory.createToast(context,e.getMessage());
//                });
        });

    }
}
