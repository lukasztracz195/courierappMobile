package com.project.courierapp.view.fragments.manager_layer.functional;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.ManagerRoadStatsFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.calculator.TimeCalculator;
import com.project.courierapp.model.di.clients.RoadClient;
import com.project.courierapp.model.dtos.response.RoadResponse;
import com.project.courierapp.model.enums.StatisticsRoadLabels;
import com.project.courierapp.view.fragments.BaseFragment;
import com.project.courierapp.view.fragments.manager_layer.ManagerFragmentTags;

import org.joda.time.Instant;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class StatsWorkerFragment extends BaseFragment {

    @Inject
    RoadClient roadClient;

    @BindView(R.id.any_chart_view)
    AnyChartView anyChartView;

    @State(ABundler.class)
    List<RoadResponse> roadResponses;

    private Pie pie;
    private long workerId;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static int MILISS_IN_HOUR = 3_600_000;

    public StatsWorkerFragment() {
    }

    public StatsWorkerFragment(long workerId) {
        this.workerId = workerId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        ManagerRoadStatsFragmentBinding managerRoadStatsFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.manager_road_stats_fragment,
                container, false);
        View mainView = managerRoadStatsFragmentBinding.getRoot();
        ButterKnife.bind(this, mainView);
        CourierApplication.getClientsComponent().inject(this);
        if (roadResponses == null) {
            downloadData();
        }
        pie = AnyChart.pie();
        anyChartView.setChart(pie);
        return mainView;
    }

    public void downloadData() {
        Disposable disposable = roadClient.getFinishedRoadsByWorkerId(workerId)
                .subscribe(this::updateDate, e -> Log.e(ManagerFragmentTags.StatsWorkerFragment, e.getMessage(), e));
        compositeDisposable.add(disposable);
    }

    private void updateDate(List<RoadResponse> roadResponses) {
        this.roadResponses = roadResponses;
        prepareDate();
    }

    private void prepareDate() {
        Map<StatisticsRoadLabels, Integer> statisticMap = new HashMap<>();
        statisticMap.put(StatisticsRoadLabels.SLOWER_THAN_GOOGLE,0);
        statisticMap.put(StatisticsRoadLabels.IN_TIME_GOOGLE,0);
        statisticMap.put(StatisticsRoadLabels.FASTER_THAN_GOOGLE,0);
        for(RoadResponse roadResponse : roadResponses){
            LocalDateTime start = roadResponse.getStartedTime();
            LocalDateTime stop = roadResponse.getFinishedTime();
            double traveledInHours = TimeCalculator.calculatedHoursBetween(start, stop);
            double expectedHours = Instant.parse(roadResponse.getExpectedTime())
                    .getMillis() / MILISS_IN_HOUR;
            double difference = expectedHours - traveledInHours;
            if(difference == 0.0){
                statisticMap.compute(StatisticsRoadLabels.IN_TIME_GOOGLE, (k,v) -> v+1);
            }else if(difference > 0.0){
                statisticMap.compute(StatisticsRoadLabels.FASTER_THAN_GOOGLE, (k,v) -> v+1);
            }else{
                statisticMap.compute(StatisticsRoadLabels.SLOWER_THAN_GOOGLE, (k,v) -> v+1);
            }
        }
        List<DataEntry> data = new ArrayList<>();
        for(StatisticsRoadLabels key : statisticMap.keySet()) {
            Integer value = statisticMap.get(key);
            data.add(new ValueDataEntry(key.toString(), value));
        }
        pie.data(data);
    }


}
