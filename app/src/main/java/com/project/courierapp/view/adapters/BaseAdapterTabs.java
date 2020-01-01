package com.project.courierapp.view.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class BaseAdapterTabs extends FragmentStatePagerAdapter {

    protected List<String> pageTitles;

    public BaseAdapterTabs(@NonNull FragmentManager fm,@NonNull List<String> titlesOfpages) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.pageTitles = titlesOfpages;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position);
    }

    @Override
    public int getCount() {
        if(pageTitles != null) {
            return pageTitles.size();
        }
        Log.i("BaseAdapterTabs", "0");
        return 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
