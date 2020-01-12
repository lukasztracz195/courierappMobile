package com.project.courierapp.view.fragments.worker_layer.pages_worker;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.WorkerMapFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.converters.HexToStringConverter;
import com.project.courierapp.model.di.clients.RoadClient;
import com.project.courierapp.model.di.clients.TrackingPointsClient;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.response.RoadResponse;
import com.project.courierapp.model.dtos.response.TrackingPointsResponse;
import com.project.courierapp.model.observer.LocationSubscriber;
import com.project.courierapp.model.singletons.LocationSigletone;
import com.project.courierapp.view.Iback.BackWithLogOutDialog;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.fragments.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
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
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WorkerMapFragment extends BaseFragment implements BackWithLogOutDialog,
        GoogleMap.OnMapClickListener, OnMapReadyCallback, LocationSubscriber {

    private GoogleMap googleMap;

    @BindView(R.id.map_view_worker_map_fragment)
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
    private boolean mapIsActivated;
    private boolean deliveryPointsMarkersIsVisible;
    private boolean deliveryPointsPolylineIsVisible;
    private boolean cameraIsSetOnWorkerPosition;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
            if (googleMap != null) {
                if (!deliveryPointsMarkersIsVisible) {
                    setMarkersDeliveryPointsOnGoogleMap();
                }
                if (!deliveryPointsPolylineIsVisible) {
                    setDeliveryPointsPolyline();
                }
                setPolylineForTrackingPoints();
            }
        }
        CourierApplication.getClientsComponent().inject(this);
        LocationSigletone.getInstance().addSubscriber(this);
        downloadRoad();
    }

    public View onCreateView(@Nonnull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        WorkerMapFragmentBinding mapWorkerFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.worker_map_fragment,
                container, false);
        mainView = mapWorkerFragmentBinding.getRoot();
        ButterKnife.bind(this, mainView);
        return mainView;
    }

    @OnClick(R.id.zoom_on_your_position)
    public void zoomOnWorkerPosition(){
        cameraIsSetOnWorkerPosition = false;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);

    }

    public void onViewCreated(View view, Bundle saveInstanceState) {

        super.onViewCreated(view, saveInstanceState);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(Objects.requireNonNull(getContext()));
        mapIsActivated = true;
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void setMarkerWithWorkerPositionOnMap(Location location) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(
                        location.getLatitude(),
                location.getLongitude()))
                .title("Worker position")
                .snippet("This is your current position")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        setCameraOnWorkerPosition(location);
    }


    private void setCameraOnWorkerPosition(Location location){
        if(!cameraIsSetOnWorkerPosition){
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
    private void setPolylineForTrackingPoints() {
        if (trackingPointsResponseList == null || trackingPointsResponseList.isEmpty()) {
            downloadTrackingPoints();
        }
        if(!trackingPointsResponseList.isEmpty()) {

            ((Runnable) () -> {
                Collections.sort(trackingPointsResponseList);
                PolylineOptions trackingPointPolyLineOptions = new PolylineOptions().clickable(false);
                for (TrackingPointsResponse trackingPointsResponse : trackingPointsResponseList) {
                    trackingPointPolyLineOptions.add(new LatLng(trackingPointsResponse.getLatitude(),
                            trackingPointsResponse.getLongitude()));
                }
                trackingPointPolyLineOptions.color(Color.YELLOW);
                Polyline trackingPoliLine = googleMap.addPolyline(trackingPointPolyLineOptions);
                trackingPoliLine.setTag("Your path so far");
            }).run();

        }
    }

    private void setMarkersDeliveryPointsOnGoogleMap() {
        if (roadResponse == null || roadResponse.getDeliveryPoints() == null) {
            downloadRoad();
        }
        if (roadResponse.getDeliveryPoints() != null) {
            ((Runnable) () -> {
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
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
                deliveryPointsMarkersIsVisible = true;
            }).run();

        }
    }

    private void setDeliveryPointsPolyline() {
        if (roadResponse == null || roadResponse.getDeliveryPoints() == null) {
            downloadRoad();
        }
        if (roadResponse.getEncodedPath() != null) {
            ((Runnable) () -> {
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
            }).run();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationSigletone.getInstance().removeSubscriber(this);
    }


    private void downloadRoad() {
        Disposable disposable = roadClient.getLastStartedRoad().subscribe(
                this::updateDataRoad, e -> Log.e(AdaptersTags.AdapterWorkersListItem,
                        e.getMessage(), e));
        compositeDisposable.add(disposable);
    }

    private void downloadTrackingPoints() {
        Disposable disposable = trackingPointsClient.getTraceStartedRoadByLoggedWorker().subscribe(
                this::updateDataTrackingPoints, e -> Log.e(AdaptersTags.AdapterWorkersListItem,
                        e.getMessage(), e));
        compositeDisposable.add(disposable);
    }

    private void updateDataRoad(RoadResponse roadResponse) {
        this.roadResponse = roadResponse;
    }

    private void updateDataTrackingPoints(List<TrackingPointsResponse> trackingPointsResponseList) {
        this.trackingPointsResponseList = trackingPointsResponseList;
    }

    @Override
    public void notifyByLocation(Location location) {
        if(mapIsActivated) {
            setMarkerWithWorkerPositionOnMap(location);
            if (!deliveryPointsMarkersIsVisible) {
                setMarkersDeliveryPointsOnGoogleMap();
            }
            if (!deliveryPointsPolylineIsVisible) {
                setDeliveryPointsPolyline();
            }
//            setPolylineForTrackingPoints();
        }
    }
}
