package com.project.courierapp.view.fragments;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.project.courierapp.view.activities.MainActivity;

import javax.annotation.Nonnull;

public class BaseFragment extends Fragment {

    protected MainActivity activity;

    protected View mainView;

    @Override
    public void onAttach(@Nonnull Context context) {
        super.onAttach(context);
        if (activity == null && context instanceof  MainActivity) {
            activity = (MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        this.activity = null;
        super.onDetach();
    }
}
