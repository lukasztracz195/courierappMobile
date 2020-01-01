package com.project.courierapp.view.fragments.worker_layer.pages_worker;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.DeliveryPointsToVisitFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.calculator.DistanceCalculator;
import com.project.courierapp.model.di.clients.RoadClient;
import com.project.courierapp.model.dtos.request.LocationRequest;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.response.RoadResponse;
import com.project.courierapp.model.service.LocationService;
import com.project.courierapp.model.store.LastStartedRoadStore;
import com.project.courierapp.view.Iback.BackWithLogOutDialog;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.adapters.adapters_worker.AdapterToVisitDeliveryPoints;
import com.project.courierapp.view.fragments.BaseFragment;
import com.project.courierapp.view.fragments.BaseFragmentTags;
import com.project.courierapp.view.fragments.base_layer.WorkerBaseFragment;
import com.project.courierapp.view.toasts.ToastFactory;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DeliveryPointsToVisitFragment extends BaseFragment implements BackWithLogOutDialog {

    @BindView(R.id.delivery_points_to_visit_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.estimated_distance_content)
    TextView estimatedDistanceContent;

    @BindView(R.id.estimated_expected_time_content)
    TextView estimatedExpectedTimeContent;

    @BindView(R.id.finish_road_bt)
    Button finishRoadButon;

    @Inject
    RoadClient roadClient;

    @State(ABundler.class)
    RoadResponse roadResponse = new RoadResponse();

    AdapterToVisitDeliveryPoints adapterToVisitDeliveryPoints;

    private Bundle savedInstanceState;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DeliveryPointsToVisitFragment() {
        CourierApplication.getClientsComponent().inject(this);
    }

    public DeliveryPointsToVisitFragment(RoadResponse roadResponse) {
        this.roadResponse = roadResponse;
        CourierApplication.getClientsComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @androidx.annotation.Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        if (LocationService.instance != null) {
            LocationService.instance.setSendingTrackingPointsIsActivated(true);
        }
        this.savedInstanceState = savedInstanceState;
        downloadDate();
        DeliveryPointsToVisitFragmentBinding deliveryPointsToVisitFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.delivery_points_to_visit_fragment,
                container, false);
        mainView = deliveryPointsToVisitFragmentBinding.getRoot();
        ButterKnife.bind(this, mainView);
        if (roadResponse.getDeliveryPoints() != null) {
            initRecyclerView();
        }
        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if(adapterToVisitDeliveryPoints == null) {
             adapterToVisitDeliveryPoints =
                    new AdapterToVisitDeliveryPoints(getContext(), savedInstanceState,
                            roadResponse.getDeliveryPoints(), finishRoadButon);
        }
        recyclerView.setAdapter(adapterToVisitDeliveryPoints);
        setActionOnFinishRoad(adapterToVisitDeliveryPoints);
    }


    private void initTextView() {
        if (roadResponse != null) {
            estimatedExpectedTimeContent.setText(roadResponse.getExpectedTime());
            if (!roadResponse.getDeliveryPoints().isEmpty()) {
                List<DeliveryPointResponse> deliveryPointResponseList = roadResponse
                        .getDeliveryPoints().stream()
                        .sorted(Comparator.comparingInt(DeliveryPointResponse::getOrder))
                        .collect(Collectors.toList());
                estimatedDistanceContent.setText(
                        String.valueOf(DistanceCalculator
                                .caluculateDistanceFromListDeliveryPoints(deliveryPointResponseList)));
            }
        }
    }

    @SuppressLint("CheckResult")
    private void setActionOnFinishRoad(AdapterToVisitDeliveryPoints adapterToVisitDeliveryPoints) {
        finishRoadButon.setOnClickListener(view -> {
            if (adapterToVisitDeliveryPoints.allPointsVisited()) {
                LocationService locationService = LocationService.instance;
                if (locationService != null) {
                    Location location = locationService.getLocation();
                    if (location != null) {
                        roadClient.finishRoad(roadResponse.getRoadId(), LocationRequest.builder()
                                .latitude(location.getLatitude())
                                .longitude(location.getLongitude())
                                .build())
                                .subscribe(response -> {
                                    LastStartedRoadStore.clear();
                                    activity.setBaseForBackStack(new WorkerBaseFragment(),
                                            BaseFragmentTags.WorkerBaseFragment);
                                }, (Throwable e) -> {
                                    ToastFactory.createToast(activity, e.getMessage());
                                });
                    }
                }
            }
        });
    }

    private void downloadDate() {
        Disposable disposable = roadClient.getLastStartedRoad().subscribe(
                this::updateData, e -> Log.e(AdaptersTags.AdapterWorkersListItem,
                        e.getMessage(), e));
        compositeDisposable.add(disposable);
    }

    private void updateData(RoadResponse roadResponse) {
        this.roadResponse = roadResponse;
        LastStartedRoadStore.saveRoadId(roadResponse.getRoadId());
        initTextView();
        initRecyclerView();
    }
}
