package com.project.courierapp.view.fragments.base_layer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.project.courierapp.R;
import com.project.courierapp.view.Iback.BackWithLogOutDialog;
import com.project.courierapp.view.adapters.adapters_manager.AdapterManagerTabsPages;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManagerBaseFragment extends Fragment implements BackWithLogOutDialog {

    @BindView(R.id.managerViewPager)
    ViewPager viewPager;

    @BindView(R.id.managerMagicIndicator)
    MagicIndicator magicIndicator;

    private View mainView;

    private final List<String> fragments = Arrays.asList("My Workers", "States roads");

    private boolean isInitialized = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (!isInitialized) {

            mainView = inflater.inflate(R.layout.manager_base_fragment, container, false);

            ButterKnife.bind(this, mainView);
//
            CommonNavigator commonNavigator = new CommonNavigator(getContext());
            commonNavigator.setAdapter(new CommonNavigatorAdapter() {

                @Override
                public int getCount() {
                    return fragments == null ? 0 : fragments.size();
                }

                @Override
                public IPagerTitleView getTitleView(Context context, final int index) {
                    ColorTransitionPagerTitleView colorTransitionPagerTitleView =
                            new ColorTransitionPagerTitleView(context);
                    colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                    colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                    colorTransitionPagerTitleView.setText(fragments.get(index));
                    colorTransitionPagerTitleView.setOnClickListener(view -> viewPager
                            .setCurrentItem(index));
                    return colorTransitionPagerTitleView;
                }

                @Override
                public IPagerIndicator getIndicator(Context context) {
                    LinePagerIndicator indicator = new LinePagerIndicator(context);
                    indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                    return indicator;
                }
            });
            magicIndicator.setNavigator(commonNavigator);
            ViewPagerHelper.bind(magicIndicator, viewPager);
            AdapterManagerTabsPages adapterTabsPager =
                    new AdapterManagerTabsPages(getFragmentManager(), fragments);
            viewPager.setOffscreenPageLimit(-1);
            viewPager.setAdapter(adapterTabsPager);


            isInitialized = true;
        }
        return mainView;
    }

    @OnClick(R.id.log_out_button)
    public void logout() {
        Objects.requireNonNull(getActivity()).onBackPressed();
    }
}
