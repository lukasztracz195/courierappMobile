package com.project.courierapp.view.adapters.adapters_worker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.project.courierapp.view.adapters.BaseAdapterTabs;
import com.project.courierapp.view.fragments.worker_layer.pages_worker.DeliveryPointsToVisitFragment;
import com.project.courierapp.view.fragments.worker_layer.pages_worker.WorkerMapFragment;

import java.util.List;

public class AdapterWorkerIsBusyTabsPages extends BaseAdapterTabs {
    public AdapterWorkerIsBusyTabsPages(@NonNull FragmentManager fm, List<String> titlesOfpage) {
        super(fm, titlesOfpage);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DeliveryPointsToVisitFragment();
            case 1:
                return new WorkerMapFragment();
            default:
                return null;
        }
    }
}
