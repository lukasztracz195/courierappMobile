package com.project.courierapp.view.holders.holders_manager;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.project.courierapp.R;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.dtos.response.Response;
import com.project.courierapp.model.dtos.response.WorkerResponse;
import com.project.courierapp.model.dtos.transfer.DeliveryPointDto;
import com.project.courierapp.view.holders.BaseHolder;
import com.project.courierapp.view.holders.IdsListBuilder;

import java.util.List;
import java.util.Objects;

import icepick.State;

public class HolderWorkerItemView extends BaseHolder {

    @State(ABundler.class)
    DeliveryPointDto deliveryPointDto = new DeliveryPointDto();

    public HolderWorkerItemView(@NonNull View itemView) {
        super(itemView);
        initTextViews(new IdsListBuilder()
                .appendTextViewId(R.id.login_worker_content)
                .appendTextViewId(R.id.status_worker_content)
                .appendTextViewId(R.id.finished_roads_content)
                .build());
    }

    @Override
    public void initTextViews(List<Integer> ids) {
        super.initTextViews(ids);
    }


    @Override
    public void setFields(Response object) {
        super.setFields(object);
        WorkerResponse workerResponse = (WorkerResponse) super.dataObject;
        Objects.requireNonNull((TextView) mapTextView.get(R.id.login_worker_content))
                .setText(workerResponse.getLogin());
        Objects.requireNonNull((TextView) mapTextView.get(R.id.status_worker_content))
                .setText(workerResponse.getStatus());
        Objects.requireNonNull((TextView) mapTextView.get(R.id.finished_roads_content))
                .setText(String.valueOf(workerResponse.getFinishedRoad()));
    }
}
