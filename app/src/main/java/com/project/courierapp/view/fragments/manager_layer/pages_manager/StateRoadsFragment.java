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
import com.project.courierapp.databinding.RoadsListFragmentBinding;
import com.project.courierapp.view.Iback.BackWithLogOutDialog;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.adapters.adapters_manager.AdapterRoadListItem;
import com.project.courierapp.view.fragments.BaseFragment;
import com.project.courierapp.view.fragments.manager_layer.ManagerFragmentTags;
import com.project.courierapp.view.fragments.manager_layer.functional.CreateRoadFragment;

import java.util.Objects;

import javax.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StateRoadsFragment extends BaseFragment implements BackWithLogOutDialog {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        RoadsListFragmentBinding roadsListFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.roads_list_fragment,
                container, false);

        View mainView = roadsListFragmentBinding.getRoot();
        RecyclerView recyclerView = mainView.findViewById(R.id.roads_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        AdapterRoadListItem adapterRoadListItem = new AdapterRoadListItem(getContext());
        recyclerView.setAdapter(adapterRoadListItem);
        ButterKnife.bind(this, mainView);
        return mainView;
    }

    @OnClick(R.id.add_new_road)
    public void addNewRoad(){
        ((MainActivity) Objects.requireNonNull(getActivity()))
                .putFragment(new CreateRoadFragment(), ManagerFragmentTags.CreateDeliveryPointFragment);
    }
}
