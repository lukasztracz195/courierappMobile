package com.project.courierapp.view.adapters.adapters_manager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.di.clients.WorkerClient;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.adapters.BaseAdapter;
import com.project.courierapp.view.holders.BaseHolder;
import com.project.courierapp.view.holders.holders_manager.HolderWorkerItemView;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;


public class AdapterWorkersListItem extends BaseAdapter {

    @Inject
    WorkerClient workerClient;

    public AdapterWorkersListItem(Context context) {
        super(context);
        CourierApplication.getClientsComponent().inject(this);
        downloadData();
    }

    @NonNull
    @Override
    public HolderWorkerItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.view = LayoutInflater.from(context).inflate(R.layout.worker_item,
                parent, false);
        return new HolderWorkerItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        holder.setFields(responses.get(position));
    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    @Override
    public void downloadData() {
        super.downloadData();
        Disposable disposable = workerClient.getWorkers()
                .subscribe(this::updateData, e -> Log.e(AdaptersTags.AdapterWorkersListItem, e.getMessage(), e));
        compositeDisposable.add(disposable);
    }
}
