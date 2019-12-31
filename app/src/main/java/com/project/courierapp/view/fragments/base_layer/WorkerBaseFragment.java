package com.project.courierapp.view.fragments.base_layer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.project.courierapp.R;
import com.project.courierapp.view.Iback.BackWithLogOutDialog;
import com.project.courierapp.view.adapters.BaseAdapterTabs;
import com.project.courierapp.view.adapters.NavigatorAdapter;
import com.project.courierapp.view.fragments.BaseFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkerBaseFragment extends BaseFragment implements BackWithLogOutDialog {
    @BindView(R.id.managerViewPager)
    ViewPager viewPager;

    @BindView(R.id.managerMagicIndicator)
    MagicIndicator magicIndicator;

    private View mainView;

    private boolean workerIsBusy;
    private BaseAdapterTabs baseAdapterTabs;
    private static final List<String> PAGES_WHEN_WORKER_IS_BUSY = Arrays.asList("Delivery points", "Map");
    private static final List<String> PAGES_WHEN_WORKER_IS_FREE = Arrays.asList("Orders", "Done orders");
    private List<String> titlesOfPages;

    public WorkerBaseFragment() {
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
        if (!isInitialized) {

            mainView = inflater.inflate(R.layout.worker_base_fragment, container,
                    false);
            ButterKnife.bind(this, mainView);
            initMagicIndicator();
            initAdapterTabs();
            initViewPager();
            isInitialized = true;
        }
        return mainView;
    }

    @OnClick(R.id.log_out_button)
    public void logout() {
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    private void initMagicIndicator(){
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(NavigatorAdapter.of(viewPager, titlesOfPages));
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private void initViewPager(){
        viewPager.setOffscreenPageLimit(-1);
//        viewPager.setAdapter(baseAdapterTabs);
    }

    private void initAdapterTabs(){
        if(workerIsBusy){
//            baseAdapterTabs = new AdapterManagerTabsPages(
//                    activity.getSupportFragmentManager(), titlesOfPages);
        }else{
//            baseAdapterTabs = new AdapterManagerTabsPages(
//                    activity.getSupportFragmentManager(), titlesOfPages);
        }

    }
}
