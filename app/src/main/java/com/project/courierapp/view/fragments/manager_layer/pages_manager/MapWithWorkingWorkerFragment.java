package com.project.courierapp.view.fragments.manager_layer.pages_manager;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.ManagerTrackWorkerFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.comparators.TracnkingPointTimeComparator;
import com.project.courierapp.model.converters.HexToStringConverter;
import com.project.courierapp.model.converters.LocationConverter;
import com.project.courierapp.model.di.clients.RoadClient;
import com.project.courierapp.model.di.clients.TrackingPointsClient;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.response.RoadResponse;
import com.project.courierapp.model.dtos.response.TrackingPointsResponse;
import com.project.courierapp.view.Iback.BackWithRemoveFromStack;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.fragments.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MapWithWorkingWorkerFragment extends BaseFragment implements BackWithRemoveFromStack,
        GoogleMap.OnMapClickListener, OnMapReadyCallback {


    private long workerId;

    @BindView(R.id.map_view_for_track_worker)
    MapView mapView;

    @Inject
    RoadClient roadClient;

    @Inject
    TrackingPointsClient trackingPointsClient;

    @State(ABundler.class)
    RoadResponse roadResponse = new RoadResponse();

    @State(ABundler.class)
    List<TrackingPointsResponse> trackingPointsResponseList = new ArrayList<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private GoogleMap googleMap;

    private boolean mapIsActivated;
    private boolean deliveryPointsMarkersIsVisible;
    private boolean trackingPoinstRoadIsVisible;
    private boolean deliveryPointsPolylineIsVisible;
    private boolean cameraIsSetOnWorkerPosition;

    public MapWithWorkingWorkerFragment() {

    }

    public MapWithWorkingWorkerFragment(Long workerId) {
        this.workerId = workerId;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        CourierApplication.getClientsComponent().inject(this);
        downloadRoad();
    }

    public View onCreateView(@Nonnull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ManagerTrackWorkerFragmentBinding managerTrackWorkerFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.manager_track_worker_fragment,
                container, false);
        mainView = managerTrackWorkerFragmentBinding.getRoot();
        ButterKnife.bind(this, mainView);

        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        cameraIsSetOnWorkerPosition = false;
    }

    public void onViewCreated(View view, Bundle saveInstanceState) {

        super.onViewCreated(view, saveInstanceState);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(Objects.requireNonNull(getContext()));
        mapIsActivated = true;
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void downloadRoad() {
        Disposable disposable = roadClient.getStartedRoadsByWorkerId(workerId).subscribe(
                this::updateDataRoad, e -> Log.e(AdaptersTags.AdapterWorkersListItem,
                        e.getMessage(), e));
        compositeDisposable.add(disposable);
    }

    private void downloadTrackingPoints() {
        Disposable disposable = trackingPointsClient.getByRoadIdTrackingResponses(roadResponse.getRoadId()).subscribe(
                this::updateDataTrackingPoints, e -> Log.e(AdaptersTags.AdapterWorkersListItem,
                        e.getMessage(), e));
        compositeDisposable.add(disposable);
    }

    private void updateDataRoad(List<RoadResponse> roadResponses) {
        if (!roadResponses.isEmpty()) {
            this.roadResponse = roadResponses.get(0);
            downloadTrackingPoints();
        }
    }

    @OnClick(R.id.track_worker_floating_point)
    public void updateTrackingRoadWorker(){
        cameraIsSetOnWorkerPosition = false;
        downloadTrackingPoints();
    }

    private void updateDataTrackingPoints(List<TrackingPointsResponse> trackingPointsResponseList) {
        if (!trackingPointsResponseList.isEmpty()) {
            this.trackingPointsResponseList = trackingPointsResponseList;
            TrackingPointsResponse trackingPointsResponse = trackingPointsResponseList.get(trackingPointsResponseList.size() - 1);
            LatLng location = new LatLng(trackingPointsResponse.getLatitude(), trackingPointsResponse.getLongitude());
            setMarkerWithWorkerPositionOnMap(location);
            setPolylineForTrackingPoints();
            setDeliveryPointsPolyline();
            setMarkersDeliveryPointsOnGoogleMap();
        }
    }

    private void setMarkersDeliveryPointsOnGoogleMap() {
        if (roadResponse == null || roadResponse.getDeliveryPoints() == null) {
            downloadRoad();
        }
        if (roadResponse.getDeliveryPoints() != null) {
            ((Runnable) () -> {
                if(!deliveryPointsMarkersIsVisible) {
                    List<DeliveryPointResponse> deliveryPointResponseList = roadResponse.getDeliveryPoints()
                            .stream()
                            .sorted(Comparator.comparingInt(DeliveryPointResponse::getOrder))
                            .collect(Collectors.toList());
                    for (DeliveryPointResponse deliveryPointResponse : deliveryPointResponseList) {
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(
                                        deliveryPointResponse.getLatitude(),
                                        deliveryPointResponse.getLongitude()))
                                .title(deliveryPointResponse.getOrder() + ". "
                                        + deliveryPointResponse.getAddress())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    }
                    deliveryPointsMarkersIsVisible = true;
                }
            }).run();

        }
    }

    private void setCameraOnWorkerPosition(Location location) {
        if (!cameraIsSetOnWorkerPosition) {
            CameraPosition workerPosition = CameraPosition.builder()
                    .target(new LatLng(
                            location.getLatitude(),
                            location.getLongitude()))
                    .zoom(16)
                    .bearing(0)
                    .tilt(45)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(workerPosition));
            cameraIsSetOnWorkerPosition = true;
        }
    }

    private void setMarkerWithWorkerPositionOnMap(LatLng location) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        location.latitude,
                        location.longitude))
                .title("Worker position")
                .snippet("This is worker current position")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        setCameraOnWorkerPosition(LocationConverter.latLngToLocation(location));
    }

    private void setPolylineForTrackingPoints() {
        if (trackingPointsResponseList == null || trackingPointsResponseList.isEmpty()) {
            downloadTrackingPoints();
        }
        if (!trackingPointsResponseList.isEmpty()) {

            ((Runnable) () -> {
                if(!trackingPoinstRoadIsVisible) {
                    trackingPointsResponseList.sort(new TracnkingPointTimeComparator());
                    PolylineOptions trackingPointPolyLineOptions = new PolylineOptions()
                            .clickable(false)
                            .color(Color.RED);
                    for (TrackingPointsResponse trackingPointsResponse : trackingPointsResponseList) {
                        trackingPointPolyLineOptions.add(new LatLng(trackingPointsResponse.getLatitude(),
                                trackingPointsResponse.getLongitude()));
                    }
                    googleMap.addPolyline(trackingPointPolyLineOptions);
                    trackingPoinstRoadIsVisible = true;
                }
            }).run();

        }
    }

    private void setDeliveryPointsPolyline() {
        if (roadResponse == null || roadResponse.getDeliveryPoints() == null) {
            downloadRoad();
        }
        if (roadResponse.getEncodedPath() != null) {
            ((Runnable) () -> {
                if(!deliveryPointsPolylineIsVisible) {
                    String decodedPoliLine = HexToStringConverter.convert(roadResponse.getEncodedPath());
                    List<Point> path = PolylineUtils.decode(
                            decodedPoliLine, 5);
                    PolylineOptions deliveryPointsPolyLineOptions = new PolylineOptions()
                            .clickable(false)
                            .color(Color.BLUE);
                    for (Point point : path) {
                        deliveryPointsPolyLineOptions.add(new LatLng(
                                point.latitude(),
                                point.longitude()));
                    }
                    googleMap.addPolyline(deliveryPointsPolyLineOptions);
                    deliveryPointsPolylineIsVisible = true;
                }
            }).run();
        }
    }
}
