package com.project.courierapp.view.fragments.base_layer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.project.courierapp.R;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.service.LocationService;
import com.project.courierapp.view.Iback.BackWithLogOutDialog;
import com.project.courierapp.view.adapters.BaseAdapterTabs;
import com.project.courierapp.view.adapters.NavigatorAdapter;
import com.project.courierapp.view.adapters.adapters_worker.AdapterWorkerIsBusyTabsPages;
import com.project.courierapp.view.adapters.adapters_worker.AdapterWorkerIsFreeTabsPages;
import com.project.courierapp.view.fragments.BaseFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class WorkerBaseFragment extends BaseFragment implements BackWithLogOutDialog {
    @BindView(R.id.managerViewPager)
    ViewPager viewPager;

    @BindView(R.id.managerMagicIndicator)
    MagicIndicator magicIndicator;

    private View mainView;

    @State(ABundler.class)
    Boolean workerIsBusy = Boolean.FALSE;

    private BaseAdapterTabs baseAdapterTabs;
    private static final List<String> PAGES_WHEN_WORKER_IS_BUSY = Arrays.asList("Delivery points", "Map");
    private static final List<String> PAGES_WHEN_WORKER_IS_FREE = Arrays.asList("Orders", "Done orders");
    private List<String> titlesOfPages;

    public WorkerBaseFragment() {
        if(workerIsBusy) {
            titlesOfPages = PAGES_WHEN_WORKER_IS_BUSY;
        }else {
            titlesOfPages = PAGES_WHEN_WORKER_IS_FREE;
        }
    }

    public WorkerBaseFragment(boolean workerIsBusy) {
        this.workerIsBusy = workerIsBusy;
        if(workerIsBusy) {
            titlesOfPages = PAGES_WHEN_WORKER_IS_BUSY;
        }else {
            titlesOfPages = PAGES_WHEN_WORKER_IS_FREE;
        }
    }

    private boolean isInitialized = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        if (!isInitialized) {
            if(workerIsBusy) {
                titlesOfPages = PAGES_WHEN_WORKER_IS_BUSY;
            }else {
                titlesOfPages = PAGES_WHEN_WORKER_IS_FREE;
            }
            mainView = inflater.inflate(R.layout.worker_base_fragment, container,
                    false);
            ButterKnife.bind(this, mainView);
            initMagicIndicator();
            initAdapterTabs();
            initViewPager();
            isInitialized = true;
        }
            final Runnable r = () -> {
                if (LocationService.instance == null) {
                    Intent intent = new Intent(activity, LocationService.class);
                    activity.startService(intent);
                }
            };
            r.run();
        return mainView;
    }

    @OnClick(R.id.log_out_button)
    public void logout() {
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void initMagicIndicator(){
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(NavigatorAdapter.of(viewPager, titlesOfPages));
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private void initViewPager(){
        viewPager.setOffscreenPageLimit(-1);
        viewPager.setAdapter(baseAdapterTabs);
    }

    private void initAdapterTabs(){
        if(workerIsBusy){
            baseAdapterTabs = new AdapterWorkerIsBusyTabsPages(
                    activity.getSupportFragmentManager(), titlesOfPages);
        }else{
            baseAdapterTabs = new AdapterWorkerIsFreeTabsPages(
                    activity.getSupportFragmentManager(), titlesOfPages);
        }

    }
}
