package com.project.courierapp.view.adapters.adapters_manager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.di.clients.RoadClient;
import com.project.courierapp.model.dtos.response.Response;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.adapters.BaseAdapter;
import com.project.courierapp.view.holders.BaseHolder;
import com.project.courierapp.view.holders.holders_manager.HolderRoad;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class AdapterRoadListItem extends BaseAdapter {

    @Inject
    RoadClient roadClient;

    public AdapterRoadListItem(Context context) {
        super(context);
        CourierApplication.getClientsComponent().inject(this);
        downloadData();
    }

    @NonNull
    @Override
    public HolderRoad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.view = LayoutInflater.from(context).inflate(R.layout.road_item,
                parent, false);
        return new HolderRoad(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        holder.setFields((Response) responses.get(position));
    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    @Override
    public void downloadData() {
        super.downloadData();
        Disposable disposable = roadClient.getAllRoads()
                .subscribe(this::updateData, e -> Log.e(AdaptersTags.AdapterWorkersListItem, e.getMessage(), e));
        compositeDisposable.add(disposable);
    }
}
