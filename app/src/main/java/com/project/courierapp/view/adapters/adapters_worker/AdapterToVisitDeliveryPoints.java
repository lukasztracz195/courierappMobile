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
import com.project.courierapp.databinding.DeliveryPointToVisitItemBinding;
import com.project.courierapp.model.di.clients.DeliveryPointsClient;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.store.LastStartedRoadStore;
import com.project.courierapp.view.adapters.Adapter;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.adapters.BaseAdapter;
import com.project.courierapp.view.holders.BaseHolder;
import com.project.courierapp.view.holders.holders_worker.HolderDeliveryPointToVisit;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class AdapterToVisitDeliveryPoints extends BaseAdapter implements Adapter {

    @BindView(R.id.visite_bt)
    Button visitButton;

    @Inject
    DeliveryPointsClient deliveryPointsClient;

    public AdapterToVisitDeliveryPoints(Context context, Bundle savedInstanceState) {
        super(context, savedInstanceState);
        CourierApplication.getClientsComponent().inject(this);
        downloadData();
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
        Disposable disposable = deliveryPointsClient.getDeliveryPointsByRoadId(lastStartedRoadId).subscribe(
                this::updateData, e -> Log.e(AdaptersTags.AdapterWorkersListItem,
                        e.getMessage(), e));
        compositeDisposable.add(disposable);
    }

    private void setActionOnVisitDeliveryPoint(DeliveryPointResponse deliveryPointResponse, int position){
//        visitButton.setOnClickListener(view -> {
//            if(!((DeliveryPointResponse )responses.get(position)).isVisited()){
//                Disposable disposable = deliveryPointsClient.visitDeliveryPoint(
//                        deliveryPointResponse.getPointId()).subscribe(responseMessage ->{
//                    ((DeliveryPointResponse) responses.get(position)).setVisited(true);
//                    super.updateData(responses);
//                }, (Throwable e) -> {
//                    ToastFactory.createToast(context,e.getMessage());
//                });
//            }
//        });

    }
}
