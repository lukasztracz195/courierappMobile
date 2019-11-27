package com.project.sopmmobileapp.view.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.project.sopmmobileapp.R;
import com.project.sopmmobileapp.view.fragments.LoginFragment;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import lombok.Getter;

@Getter
public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getName();
    private static final String CURRENT_FRAGMENT_TAG = "currentFragment";

    public static MainActivity instance = null;

    private Fragment currentFragment;

    @BindView(R.id.layout_on_fragments)
    public FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, CURRENT_FRAGMENT_TAG);
        } else {
            this.currentFragment = new LoginFragment();
            this.changeFragment(this.currentFragment);
        }
        this.setSdkPolicy();
        instance = this;
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG, currentFragment);
    }

    private void setSdkPolicy() {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onBackPressed() {
        if (this.canBack()) {
            super.getSupportFragmentManager().popBackStackImmediate();
        }
    }

    private boolean canBack() {
        return super.getSupportFragmentManager().getBackStackEntryCount() > 1;
    }

    public void changeFragment(final Fragment fragment) {
        super.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_on_fragments, fragment)
                .addToBackStack(currentFragment.getClass().toString())
                .commit();

        this.currentFragment = fragment;
    }

    public void setBaseForBackStack(final Fragment fragment) {
        this.clearBackStack();

        super.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_on_fragments, fragment)
                .addToBackStack(currentFragment.getClass().toString())
                .commit();

        this.currentFragment = fragment;
    }

    private void clearBackStack() {
        while (super.getSupportFragmentManager().popBackStackImmediate()) ;
    }

    public void putFragment(final Fragment fragment) {
        super.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_on_fragments, fragment)
                .attach(fragment)
                .commit();
        this.currentFragment = fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        instance = this;
    }

    @Override
    public void onPause() {
        super.onPause();
        instance = null;
    }

}
