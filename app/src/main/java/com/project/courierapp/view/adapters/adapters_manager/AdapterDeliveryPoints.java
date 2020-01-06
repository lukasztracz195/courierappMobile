package com.project.courierapp.view.adapters.adapters_manager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.DeliveryPointHolderBinding;
import com.project.courierapp.model.di.clients.DeliveryPointsClient;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.transfer.DeliveryPointDto;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.adapters.BaseAdapter;
import com.project.courierapp.view.fragments.manager_layer.ManagerFragmentTags;
import com.project.courierapp.view.fragments.manager_layer.functional.EditDeliveryPointFragment;
import com.project.courierapp.view.holders.BaseHolder;
import com.project.courierapp.view.holders.holders_manager.HolderDeliveryPoint;
import com.project.courierapp.view.toasts.ToastFactory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class AdapterDeliveryPoints extends BaseAdapter {

    @BindView(R.id.edit_bt)
    ImageButton editButton;

    @BindView(R.id.delete_bt)
    ImageButton deleteButton;

    @Inject
    DeliveryPointsClient deliveryPointsClient;

    public AdapterDeliveryPoints(Context context, List<DeliveryPointResponse> deliveryPointsDto,
                                 Bundle savedInstanceState) {
        super(context, savedInstanceState);
        if (!responses.equals(deliveryPointsDto)) {
            responses = deliveryPointsDto;
        }
    }

    @NonNull
    @Override
    public HolderDeliveryPoint onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        super.onCreateViewHolder(viewGroup, viewType);
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
        DeliveryPointResponse deliveryPointDto = (DeliveryPointResponse) responses.get(position);
        setActionOnEditDeliveryPoint(deliveryPointDto, position);
        setActionOnDeleteDeliveryPoint(position);
        holder.setFields((DeliveryPointResponse) responses.get(position));
    }

    public void addPoint() {
        responses.add(new DeliveryPointResponse());
        super.notifyDataSetChanged();
    }

    public void removePoint() {
        if (!responses.isEmpty()) {
            responses.remove(responses.size() - 1);
            super.notifyDataSetChanged();
        }
    }

    public void setActionOnEditDeliveryPoint(DeliveryPointResponse deliveryPointDto, int position) {
        editButton.setOnClickListener(view -> {
            EditDeliveryPointFragment createDeliveryPointFragment =
                    new EditDeliveryPointFragment(responses, position);
            createDeliveryPointFragment.setDeliveryPointDto(new DeliveryPointDto(deliveryPointDto));

            MainActivity.instance.putFragment(createDeliveryPointFragment,
                    ManagerFragmentTags.CreateRoadFragment);
        });
    }


    public void setActionOnDeleteDeliveryPoint(int position) {
        deleteButton.setOnClickListener(view -> {
            if (position >= 0 && position < responses.size()) {
                Disposable disposable = deliveryPointsClient.deleteDeliveryPointById(
                        ((DeliveryPointResponse) responses.remove(position)).getPointId())

                        .subscribe(deleted -> {
                            if (deleted) {
                                ToastFactory.createToast(context, "Delivery point was deleted");
                            }
                        }, (Throwable e) -> {
                            ToastFactory.createToast(context, "Delivery point was deleted");
                        });
                compositeDisposable.add(disposable);
                super.notifyItemRemoved(position);
            } else {
                if (position != responses.size() && !responses.isEmpty()) {
                    Disposable disposable = deliveryPointsClient.deleteDeliveryPointById(
                            ((DeliveryPointResponse) responses.remove(0)).getPointId())
                            .subscribe(deleted -> {
                                if (deleted) {
                                    ToastFactory.createToast(context, "Delivery point was deleted");
                                }
                            }, (Throwable e) -> {
                                ToastFactory.createToast(context, "Delivery point was deleted");
                            });
                    compositeDisposable.add(disposable);
                    super.notifyItemRemoved(0);
                }
            }
        });
    }

    public void removePoint(int position) {
        if (!responses.isEmpty()) {
            responses.remove(position);
            super.notifyItemRemoved(position);
        }

    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    public List<DeliveryPointResponse> getDeliveryPointResponseList() {
        return responses;
    }
}
