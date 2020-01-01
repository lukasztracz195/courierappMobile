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
import com.project.courierapp.databinding.RoadsToStartFragmentBinding;
import com.project.courierapp.view.Iback.BackWithLogOutDialog;
import com.project.courierapp.view.adapters.adapters_worker.AdapterRoadsToStart;
import com.project.courierapp.view.fragments.BaseFragment;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoadsToStartFragment extends BaseFragment implements BackWithLogOutDialog {

    @BindView(R.id.roads_to_start_recyclerview)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        RoadsToStartFragmentBinding roadsToStartFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.roads_to_start_fragment,
                container, false);
        mainView = roadsToStartFragmentBinding.getRoot();
        ButterKnife.bind(this, mainView);
        initRecyclerView(savedInstanceState);
        return mainView;
    }

    private void initRecyclerView(Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        AdapterRoadsToStart adapterFinishedRoads =
                new AdapterRoadsToStart(getContext(), savedInstanceState);
        recyclerView.setAdapter(adapterFinishedRoads);
    }
}
