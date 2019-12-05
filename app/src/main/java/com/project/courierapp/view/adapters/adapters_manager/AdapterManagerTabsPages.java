package com.project.courierapp.view.adapters.adapters_manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.project.courierapp.view.fragments.manager_layer.pages_manager.StateRoadsFragment;
import com.project.courierapp.view.fragments.manager_layer.pages_manager.WorkersListFragment;

import java.util.List;

public class AdapterManagerTabsPages extends FragmentStatePagerAdapter {

    private  List<String> titles;

    public AdapterManagerTabsPages(FragmentManager fragmentManager, List<String> titles) {
        super(fragmentManager);
        this.titles = titles;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new WorkersListFragment();
            case 1:
                return new StateRoadsFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
