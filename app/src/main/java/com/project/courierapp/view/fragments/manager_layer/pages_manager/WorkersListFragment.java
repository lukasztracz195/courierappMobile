package com.project.courierapp.view.fragments.manager_layer.pages_manager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.courierapp.R;
import com.project.courierapp.databinding.WorkerListFragmentBinding;
import com.project.courierapp.view.Iback.BackWithLogOutDialog;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.adapters.adapters_manager.AdapterWorkersListItem;
import com.project.courierapp.view.fragments.BaseFragment;
import com.project.courierapp.view.fragments.manager_layer.ManagerFragmentTags;
import com.project.courierapp.view.fragments.manager_layer.functional.RegisterWorkerFragment;

import java.util.Objects;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkersListFragment extends BaseFragment implements BackWithLogOutDialog {

    @BindView(R.id.workers_recyclerview)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        WorkerListFragmentBinding mySurveysFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.worker_list_fragment,
                container, false);
        mainView = mySurveysFragmentBinding.getRoot();
        ButterKnife.bind(this, mainView);
        initRecyclerView();
        return mainView;
    }

    @OnClick(R.id.register_new_worker)
    public void registerNewWorker(){
        ((MainActivity) Objects.requireNonNull(getActivity()))
                .putFragment(new RegisterWorkerFragment(),
                        ManagerFragmentTags.RegisterWorkerFragment);
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        AdapterWorkersListItem adapterWorkersListItem = new AdapterWorkersListItem(getContext());
        recyclerView.setAdapter(adapterWorkersListItem);
    }
}
