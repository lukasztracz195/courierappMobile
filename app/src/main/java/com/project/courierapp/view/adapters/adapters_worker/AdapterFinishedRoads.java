package com.project.courierapp.view.adapters.adapters_worker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.FinishedRoadItemBinding;
import com.project.courierapp.model.di.clients.RoadClient;
import com.project.courierapp.model.dtos.response.RoadResponse;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.adapters.BaseAdapter;
import com.project.courierapp.view.holders.BaseHolder;
import com.project.courierapp.view.holders.holders_worker.HolderFinishedRoad;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class AdapterFinishedRoads extends BaseAdapter {


    @Inject
    RoadClient roadClient;

    public AdapterFinishedRoads(Context context, Bundle savedInstanceState) {
        super(context, savedInstanceState);
        CourierApplication.getClientsComponent().inject(this);
        downloadData();
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        super.onCreateViewHolder(viewGroup, viewType);
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        FinishedRoadItemBinding finishedRoadItemBinding = DataBindingUtil
                .inflate(layoutInflater,
                        R.layout.finished_road_item, viewGroup, false);
        view = finishedRoadItemBinding.getRoot();
        ButterKnife.bind(this, view);
        return new HolderFinishedRoad(finishedRoadItemBinding);
    }


    @Override
    public void onBindViewHolder(@NotNull BaseHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.setFields((RoadResponse) responses.get(position));
    }

    @Override
    public void downloadData() {
        super.downloadData();
        Disposable disposable = roadClient.getAllFinishedRoadForLoggedWorker().subscribe(
                this::updateData, e -> Log.e(AdaptersTags.AdapterWorkersListItem,
                        e.getMessage(), e));
        compositeDisposable.add(disposable);
    }
}
