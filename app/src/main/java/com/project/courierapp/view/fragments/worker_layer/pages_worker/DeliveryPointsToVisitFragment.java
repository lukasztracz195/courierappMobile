package com.project.courierapp.view.fragments.worker_layer.pages_worker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.courierapp.R;
import com.project.courierapp.databinding.DeliveryPointsToVisitFragmentBinding;
import com.project.courierapp.view.Iback.BackWithLogOutDialog;
import com.project.courierapp.view.adapters.adapters_worker.AdapterToVisitDeliveryPoints;
import com.project.courierapp.view.fragments.BaseFragment;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliveryPointsToVisitFragment extends BaseFragment implements BackWithLogOutDialog {

    @BindView(R.id.delivery_points_to_visit_recyclerview)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        DeliveryPointsToVisitFragmentBinding deliveryPointsToVisitFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.delivery_points_to_visit_fragment,
                container, false);
        mainView = deliveryPointsToVisitFragmentBinding.getRoot();
        ButterKnife.bind(this, mainView);
        initRecyclerView(savedInstanceState);
        return mainView;
    }

    private void initRecyclerView(Bundle savedInstanceState){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        AdapterToVisitDeliveryPoints adapterRoadListItem =
                new AdapterToVisitDeliveryPoints(getContext(), savedInstanceState);
        recyclerView.setAdapter(adapterRoadListItem);
    }
}
