package com.project.courierapp.view.fragments.base_layer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ServiceCompat;
import androidx.viewpager.widget.ViewPager;

import com.project.courierapp.R;
import com.project.courierapp.model.service.LocationService;
import com.project.courierapp.view.Iback.BackWithLogOutDialog;
import com.project.courierapp.view.adapters.NavigatorAdapter;
import com.project.courierapp.view.adapters.adapters_manager.AdapterManagerTabsPages;
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

public class ManagerBaseFragment extends BaseFragment implements BackWithLogOutDialog {

    @BindView(R.id.manager_view_pager)
    ViewPager viewPager;

    @BindView(R.id.manager_magic_indicator)
    MagicIndicator magicIndicator;

    private View mainView;

    private final List<String> titlesOfPages = Arrays.asList("My Workers", "States roads");

    private boolean isInitialized = false;

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if(LocationService.instance != null){
            LocationService.instance.setSendingTrackingPointsIsActivated(false);
            LocationService.instance.stopForeground(ServiceCompat.STOP_FOREGROUND_REMOVE);
        }
        if (!isInitialized) {
            mainView = inflater.inflate(R.layout.manager_base_fragment, container,
                    false);
            ButterKnife.bind(this, mainView);
            initMagicIndicator();
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
        AdapterManagerTabsPages adapterTabsPager =
                new AdapterManagerTabsPages(getFragmentManager(), titlesOfPages);
        viewPager.setOffscreenPageLimit(-1);
        viewPager.setAdapter(adapterTabsPager);
    }
}
