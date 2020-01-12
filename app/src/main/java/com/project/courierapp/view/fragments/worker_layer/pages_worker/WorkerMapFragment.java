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
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.utils.PolylineUtils;
import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.MapWorkerFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.di.clients.RoadClient;
import com.project.courierapp.model.di.clients.TrackingPointsClient;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.response.RoadResponse;
import com.project.courierapp.model.dtos.response.TrackingPointsResponse;
import com.project.courierapp.view.Iback.BackWithLogOutDialog;
import com.project.courierapp.view.adapters.AdaptersTags;
import com.project.courierapp.view.fragments.BaseFragment;

import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor
public class WorkerMapFragment extends BaseFragment implements BackWithLogOutDialog, GoogleMap.OnMapClickListener, OnMapReadyCallback {

    GoogleMap googleMap;

    public static boolean mapIsActivated;
    public static WorkerMapFragment instance;

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
    private boolean deliveryPointsMarkersVisible;
    private boolean deliveryPointsPolilineVisible;

    public static final int PATTERN_DASH_LENGTH_PX = 20;
    public static final int PATTERN_GAP_LENGTH_PX = 20;
    public static final PatternItem DOT = new Dot();
    public static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    public static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    public static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
            if (googleMap != null) {
                if (!deliveryPointsMarkersVisible) {
                    setMarkersDeliveryPointsOnGoogleMap();
                }
                if (!deliveryPointsPolilineVisible) {
                    setDeliveryPointsPoliline();
                }
                setPolyLineTrackingPoints();
            }
        }
        CourierApplication.getClientsComponent().inject(this);
        downloadRoad();
//        downloadTrackingPoints();
    }

    public View onCreateView(@Nonnull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MapWorkerFragmentBinding mapWorkerFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.map_worker_fragment,
                container, false);
        mainView = mapWorkerFragmentBinding.getRoot();
        ButterKnife.bind(this, mainView);
        return mainView;
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
        instance = this;
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void updateMap(Location location) {
        setMyPositionOnGoogleMap(location);
        if (!deliveryPointsMarkersVisible) {
            setMarkersDeliveryPointsOnGoogleMap();
        }
        if (!deliveryPointsPolilineVisible) {
            setDeliveryPointsPoliline();
        }
        setPolyLineTrackingPoints();
    }

    private void setMyPositionOnGoogleMap(Location location) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),
                location.getLongitude())).title("Worker position").snippet("This is my position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

        CameraPosition myposition = CameraPosition.builder().target(
                new LatLng(location.getLatitude(),
                        location.getLongitude()))
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(myposition));
    }


    private void setPolyLineTrackingPoints() {
        if (trackingPointsResponseList == null || trackingPointsResponseList.isEmpty()) {
            downloadTrackingPoints();
        }
        if(!trackingPointsResponseList.isEmpty()) {
            Collections.sort(trackingPointsResponseList);
            PolylineOptions trackingPointPolyLineOptions = new PolylineOptions().clickable(false);
            for (TrackingPointsResponse trackingPointsResponse : trackingPointsResponseList) {
                trackingPointPolyLineOptions.add(new LatLng(trackingPointsResponse.getLatitude(),
                        trackingPointsResponse.getLongitude()));
            }
            trackingPointPolyLineOptions.color(Color.YELLOW).width(10).pattern(PATTERN_POLYGON_ALPHA);
            Polyline trackingPoliLine = googleMap.addPolyline(trackingPointPolyLineOptions);
            trackingPoliLine.setTag("Your path so far");
        }
    }

    private void setMarkersDeliveryPointsOnGoogleMap() {
        if (roadResponse == null || roadResponse.getDeliveryPoints() == null) {
            downloadRoad();
        }
        if (roadResponse.getDeliveryPoints() != null) {
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
            deliveryPointsMarkersVisible = true;
        }
    }

    private void setDeliveryPointsPoliline() {
        if (roadResponse == null || roadResponse.getDeliveryPoints() == null) {
            downloadRoad();
        }
        if (roadResponse.getEncodedPath() != null) {
            String decodedPoliLine = decodeHEX(roadResponse.getEncodedPath());
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
            deliveryPointsPolilineVisible = true;
        }
    }


    @SneakyThrows
    private String decodeHEX(String hex) {
        byte[] bytes = Hex.decodeHex(hex.toCharArray());
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapIsActivated = false;
        instance = null;
    }


    public void downloadRoad() {
        Disposable disposable = roadClient.getLastStartedRoad().subscribe(
                this::updateDataRoad, e -> Log.e(AdaptersTags.AdapterWorkersListItem,
                        e.getMessage(), e));
        compositeDisposable.add(disposable);
    }

    public void downloadTrackingPoints() {
        Disposable disposable = trackingPointsClient.getTraceStartedRoadByLoggedWorker().subscribe(
                this::updateDataTrackingPoints, e -> Log.e(AdaptersTags.AdapterWorkersListItem,
                        e.getMessage(), e));
        compositeDisposable.add(disposable);
    }

    public void updateDataRoad(RoadResponse roadResponse) {
        this.roadResponse = roadResponse;
    }

    public void updateDataTrackingPoints(List<TrackingPointsResponse> trackingPointsResponseList) {
        this.trackingPointsResponseList = trackingPointsResponseList;
    }
}
