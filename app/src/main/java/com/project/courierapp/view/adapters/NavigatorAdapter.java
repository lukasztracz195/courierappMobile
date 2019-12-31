package com.project.courierapp.view.adapters;

import android.content.Context;
import android.graphics.Color;

import androidx.viewpager.widget.ViewPager;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.List;

public class NavigatorAdapter extends CommonNavigatorAdapter {

    private ViewPager viewPager;
    private List<String> titlesOfPages;

    private NavigatorAdapter(ViewPager viewPager, List<String> titlesOfPages) {
        this.viewPager = viewPager;
        this.titlesOfPages = titlesOfPages;
    }

    public static NavigatorAdapter of(ViewPager viewPager, List<String> titlesOfPages) {
        return new NavigatorAdapter(viewPager, titlesOfPages);
    }

    @Override
    public int getCount() {
        return titlesOfPages == null ? 0 : titlesOfPages.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
        ColorTransitionPagerTitleView colorTransitionPagerTitleView =
                new ColorTransitionPagerTitleView(context);
        colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
        colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
        colorTransitionPagerTitleView.setText(titlesOfPages.get(index));
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
}
