package com.project.courierapp.view.fragments.manager_layer.functional;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.CreateRoadFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.di.clients.DeliveryPointsClient;
import com.project.courierapp.model.di.clients.RoadClient;
import com.project.courierapp.model.di.clients.WorkerClient;
import com.project.courierapp.model.dtos.request.AddRoadRequest;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.response.WorkerResponse;
import com.project.courierapp.view.Iback.BackWithRemoveFromStack;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.adapters.adapters_manager.AdapterDeliveryPoints;
import com.project.courierapp.view.fragments.BaseFragment;
import com.project.courierapp.view.fragments.base_layer.ManagerBaseFragment;
import com.project.courierapp.view.fragments.manager_layer.ManagerFragmentTags;
import com.project.courierapp.view.toasts.ToastFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CreateRoadFragment extends BaseFragment implements BackWithRemoveFromStack {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @BindView(R.id.error_message)
    TextView errorMessage;

    @BindView(R.id.workers_spinner)
    Spinner workersSpinner;

    @BindView(R.id.create_road)
    Button createRoad;

    @BindView(R.id.remove_point_bt)
    Button removePoint;

    @BindView(R.id.add_point_bt)
    Button addPoint;

    @BindView(R.id.delivery_points_recyclerview)
    RecyclerView deliveryPointsRecyclerView;

    @State(ABundler.class)
    List<WorkerResponse> workerResponseList = new ArrayList<>();

    @State(ABundler.class)
    List<DeliveryPointResponse> deliveryPointResponseList = new ArrayList<>();

    @Inject
    WorkerClient workerClient;

    @Inject
    DeliveryPointsClient deliveryPointsClient;

    @Inject
    RoadClient roadClient;

     private AdapterDeliveryPoints adapterDeliveryPoints;

    public CreateRoadFragment() {

    }

    public CreateRoadFragment(List<DeliveryPointResponse> deliveryPointResponseList) {
        this.deliveryPointResponseList = deliveryPointResponseList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        CreateRoadFragmentBinding createSurveyFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.create_road_fragment, container, false);

        mainView = createSurveyFragmentBinding.getRoot();
        ButterKnife.bind(this, mainView);
        CourierApplication.getClientsComponent().inject(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        deliveryPointsRecyclerView.setLayoutManager(layoutManager);
        adapterDeliveryPoints = new AdapterDeliveryPoints(getContext(), deliveryPointResponseList, savedInstanceState);
        deliveryPointsRecyclerView.setAdapter(adapterDeliveryPoints);
        if (workerResponseList == null || workerResponseList.isEmpty()) {
            reloadWorkers();
        }
        if (deliveryPointResponseList.isEmpty()) {
            createRoad.setEnabled(false);
            removePoint.setEnabled(false);
        }
        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @OnClick(R.id.add_point_bt)
    public void add() {
        ((MainActivity) Objects.requireNonNull(getActivity())).putFragment(
                new CreateDeliveryPointFragment(deliveryPointResponseList),
                ManagerFragmentTags.CreateDeliveryPointFragment);
        createRoad.setEnabled(true);
        removePoint.setEnabled(true);
    }


    @SuppressLint("CheckResult")
    @OnClick(R.id.remove_point_bt)
    public void removeLast() {
        if (!deliveryPointResponseList.isEmpty()) {
            DeliveryPointResponse deliveryPointToDeleteFromServer = deliveryPointResponseList
                    .get(deliveryPointResponseList.size() - 1);
            deliveryPointsClient.deleteDeliveryPointById(deliveryPointToDeleteFromServer.getPointId())
                    .subscribe(response -> {
                        if (response) {
                            adapterDeliveryPoints.removePoint();
                            deliveryPointResponseList = adapterDeliveryPoints
                                    .getDeliveryPointResponseList();
                            if (deliveryPointResponseList.isEmpty()) {
                                createRoad.setEnabled(false);
                            }
                            ToastFactory.createToast(activity, " Delivery point was deleted");
                        }
                    }, (Throwable e) -> {
                        errorMessage.setText(e.getMessage());
                    });
        }
        if (deliveryPointResponseList.isEmpty()) {
            createRoad.setEnabled(false);
        }
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.cancel_bt)
    public void cancel() {
        //TODO THINKING ABOUT ALERT DIALOG ON EXIT WITH UNSAVED DELIVERY POINTS
        for (DeliveryPointResponse deliveryPointResponse : deliveryPointResponseList) {
            deliveryPointsClient.deleteDeliveryPointById((deliveryPointResponse).getPointId())
                    .subscribe(deleted -> {
                        if (deleted) {
                            ToastFactory.createToast(activity, "Delivery point was deleted");
                        }
                    }, (Throwable e) -> {
                        ToastFactory.createToast(activity, "Delivery point was deleted");
                    });
        }
        while (!deliveryPointResponseList.isEmpty()) {
            removeLast();
        }
        activity.onBackPressed();
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.create_road)
    public void createRoad() {
        if (!deliveryPointResponseList.isEmpty()) {
            Disposable disposable = roadClient.add(AddRoadRequest.builder().deliveryPointsIds(deliveryPointResponseList.stream()
                    .map(DeliveryPointResponse::getPointId).collect(Collectors.toList()))
                    .workerId(findWorkerIdByLogin((String) workersSpinner.getSelectedItem()))
                    .build()).subscribe(response -> {
                ((MainActivity) Objects.requireNonNull(getActivity()))
                        .putFragment(new ManagerBaseFragment(),
                                ManagerFragmentTags.WorkersListFragment);
            }, (Throwable e) -> {
                errorMessage.setText(e.getMessage());
            });
            compositeDisposable.add(disposable);
        }
    }

    @OnClick(R.id.reload_workers_spinner)
    void reloadWorkers() {
        Disposable disposable = workerClient.getWorkers()
                .subscribe(this::updateData, e -> Log.e(AdaptersTags.AdapterWorkersListItem, e.getMessage(), e));
        compositeDisposable.add(disposable);
        setSpinnerValues();
    }

    private void setSpinnerValues() {
        String[] workerUserNames = workerResponseList.stream()
                .map(WorkerResponse::getLogin)
                .collect(Collectors.toList()).toArray(new String[workerResponseList.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CourierApplication.getContext(),
                android.R.layout.simple_spinner_item, workerUserNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workersSpinner.setAdapter(adapter);
    }

    private Long findWorkerIdByLogin(String login) {
        return workerResponseList.stream().filter(f -> f.getLogin().equals(login))
                .findFirst().get().getWorkerId();
    }

    private void updateData(List<WorkerResponse> workerResponseList) {
        this.workerResponseList = workerResponseList;
        if (workerResponseList.isEmpty()) {
            addPoint.setEnabled(false);
        }
        setSpinnerValues();
    }
}
