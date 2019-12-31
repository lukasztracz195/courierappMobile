package com.project.courierapp.view.adapters.adapters_manager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.project.courierapp.view.adapters.BaseAdapterTabs;
import com.project.courierapp.view.fragments.manager_layer.pages_manager.StateRoadsFragment;
import com.project.courierapp.view.fragments.manager_layer.pages_manager.WorkersListFragment;

import java.util.List;

public class AdapterManagerTabsPages extends BaseAdapterTabs {

    public AdapterManagerTabsPages(FragmentManager fragmentManager, List<String> titles) {
        super(fragmentManager,titles);
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
}
