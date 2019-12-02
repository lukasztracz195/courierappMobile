package com.project.courierapp.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.ChangePasswordFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.constans.Roles;
import com.project.courierapp.model.deserializers.JwtDeserializer;
import com.project.courierapp.model.di.clients.ChangePasswordClient;
import com.project.courierapp.model.dtos.request.ChangePasswordRequest;
import com.project.courierapp.model.exceptions.BadRequestException;
import com.project.courierapp.model.exceptions.LoginException;
import com.project.courierapp.model.store.CredentialsStore;
import com.project.courierapp.model.store.TokenStore;
import com.project.courierapp.view.activities.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.Disposable;


public class ChangePasswordFragment extends Fragment {

    @BindView(R.id.error_message)
    TextView errorMessage;

    @Inject
    ChangePasswordClient changePasswordClient;

    @State(ABundler.class)
    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }

        ChangePasswordFragmentBinding changePasswordFragmentBinding = DataBindingUtil
                .inflate(inflater, R.layout.change_password_fragment,
                        container, false);
        View mainView = changePasswordFragmentBinding.getRoot();
        changePasswordFragmentBinding.setChangePasswordRequest(this.changePasswordRequest);
        ButterKnife.bind(this, mainView);

        CourierApplication.getClientsComponent().inject(this);
        return mainView;
    }


    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @OnClick(R.id.change_password_button)
    public void changePassword(){
        Disposable disposable = this.changePasswordClient.changePassword(this.changePasswordRequest)
                .subscribe(token -> {
                    Log.i(TAG, "Logged in");
                    TokenStore.saveToken(token);
                    CredentialsStore.saveCredentials(this.credentialsRequest);
                    Map<String, String> map = JwtDeserializer.decoded(TokenStore.getToken());
                    String role = map.get("\"roles\"");
                    if (role.contains(Roles.MANAGER)) {
                        Log.i("LoginFragment", Roles.MANAGER);
                    }
                    if (role.contains(Roles.WORKER)) {
                        Log.i("LoginFragment", Roles.WORKER);
                    } else {
                        Log.i("LoginFragment", Roles.TEMPORARY);
                        ((MainActivity) getActivity()).putFragment(new ChangePasswordFragment());
                    }
                }, (Throwable e) -> {
                    if (e instanceof LoginException) {
                        Log.i(TAG, "LoginException", e);
                        this.errorMessage.setText(getString(R.string.login_error));
                    } else if (e instanceof BadRequestException) {
                        Log.i(TAG, "Server error", e);
                        this.errorMessage.setText(getString(R.string.server_error));
                    }
                });
    }
}
