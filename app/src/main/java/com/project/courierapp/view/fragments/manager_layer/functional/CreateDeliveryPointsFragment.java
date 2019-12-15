package com.project.courierapp.view.fragments.manager_layer.functional;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.CreateDeliveryPointsFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.di.clients.WorkerClient;
import com.project.courierapp.model.dtos.response.WorkerResponse;
import com.project.courierapp.model.dtos.transfer.DeliveryPointDto;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.adapters.adapters_manager.AdapterDeliveryPoints;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CreateDeliveryPointsFragment extends Fragment {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private List<WorkerResponse> workerResponseList = new ArrayList<>();

    @BindView(R.id.error_message)
    TextView errorMessage;

    @BindView(R.id.workers_spinner)
    Spinner workersSpinner;

    @BindView(R.id.create_road)
    Button createRoad;

    @BindView(R.id.delivery_points_recyclerview)
    RecyclerView deliveryPointsRecyclerView;

    @State(ABundler.class)
    List<DeliveryPointDto> deliveryPointDtos = new ArrayList<>();

    @Inject
    WorkerClient workerClient;

    private AdapterDeliveryPoints adapterDeliveryPoints;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        CreateDeliveryPointsFragmentBinding createSurveyFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.create_delivery_points_fragment, container, false);

        View mainView = createSurveyFragmentBinding.getRoot();
        ButterKnife.bind(this, mainView);
        CourierApplication.getClientsComponent().inject(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        deliveryPointsRecyclerView.setLayoutManager(layoutManager);
        adapterDeliveryPoints = new AdapterDeliveryPoints(getContext(), deliveryPointDtos,savedInstanceState);
        deliveryPointsRecyclerView.setAdapter(adapterDeliveryPoints);
        reloadWorkers();
        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        adapterDeliveryPoints.onSaveInstanceState(outState);
        deliveryPointDtos = adapterDeliveryPoints.getDeliveryPointDtoList();
        Icepick.saveInstanceState(this, outState);
    }

    @OnClick(R.id.add_point_bt)
    public void add() {
        adapterDeliveryPoints.addPoint();
        createRoad.setEnabled(true);
    }


    @OnClick(R.id.remove_point_bt)
    public void removeLast() {
        adapterDeliveryPoints.removePoint();
        if(deliveryPointDtos.isEmpty()){
            createRoad.setEnabled(false);
        }
    }

    @OnClick(R.id.cancel_bt)
    public void cancel() {

    }

    @OnClick(R.id.create_road)
    public void createRoad() {

    }

    @OnClick(R.id.reload_workers_spinner)
    public void reloadWorkers() {
        Disposable disposable = workerClient.getWorkers()
                .subscribe(this::updateData, e -> Log.e(AdaptersTags.AdapterWorkersListItem, e.getMessage(), e));
        compositeDisposable.add(disposable);
        setSpinnerValues();
    }

    private void setSpinnerValues() {
        String[] workerUsernames = workerResponseList.stream()
                .map(WorkerResponse::getLogin)
                .collect(Collectors.toList()).toArray(new String[workerResponseList.size()]);
        ArrayAdapter<String>adapter = new ArrayAdapter<>(CourierApplication.getContext(),
                android.R.layout.simple_spinner_item,workerUsernames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workersSpinner.setAdapter(adapter);
    }

    private void updateData(List<WorkerResponse> workerResponseList) {
        this.workerResponseList = workerResponseList;
        setSpinnerValues();
    }
}
