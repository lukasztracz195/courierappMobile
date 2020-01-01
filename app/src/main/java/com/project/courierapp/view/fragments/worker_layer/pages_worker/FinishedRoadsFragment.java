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
import com.project.courierapp.databinding.FinishedRoadsFragmentBinding;
import com.project.courierapp.view.Iback.BackWithLogOutDialog;
import com.project.courierapp.view.adapters.adapters_worker.AdapterFinishedRoads;
import com.project.courierapp.view.fragments.BaseFragment;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinishedRoadsFragment extends BaseFragment implements BackWithLogOutDialog {

    @BindView(R.id.finished_roads_recyclerview)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        FinishedRoadsFragmentBinding finishedRoadsFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.finished_roads_fragment,
                container, false);
        mainView = finishedRoadsFragmentBinding.getRoot();
        ButterKnife.bind(this, mainView);
        initRecyclerView(savedInstanceState);
        return mainView;
    }

    private void initRecyclerView(Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        AdapterFinishedRoads adapterFinishedRoads =
                new AdapterFinishedRoads(getContext(), savedInstanceState);
        recyclerView.setAdapter(adapterFinishedRoads);
    }
}
