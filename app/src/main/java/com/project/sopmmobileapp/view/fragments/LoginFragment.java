package com.project.sopmmobileapp.view.fragments;

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

import com.project.sopmmobileapp.R;
import com.project.sopmmobileapp.applications.CourierApplication;
import com.project.sopmmobileapp.databinding.LoginFragmentBinding;
import com.project.sopmmobileapp.model.bundlers.ABundler;
import com.project.sopmmobileapp.model.constans.Roles;
import com.project.sopmmobileapp.model.deserializers.JwtDeserializer;
import com.project.sopmmobileapp.model.di.clients.GpsClient;
import com.project.sopmmobileapp.model.di.clients.LoginClient;
import com.project.sopmmobileapp.model.dtos.request.CredentialsRequest;
import com.project.sopmmobileapp.model.exceptions.BadRequestException;
import com.project.sopmmobileapp.model.exceptions.LoginException;
import com.project.sopmmobileapp.model.store.CredentialsStore;
import com.project.sopmmobileapp.model.store.TokenStore;
import com.project.sopmmobileapp.model.validators.PasswordValidator;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LoginFragment extends Fragment {

    private final static String TAG = LoginFragment.class.getName();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String LOGIN_SUCCESSFUL_MESSAGE = "Login is successful";

    @BindView(R.id.error_message)
    TextView errorMessage;

    @Inject
    LoginClient loginClient;

    @Inject
    GpsClient gpsClient;

    @State(ABundler.class)
    CredentialsRequest credentialsRequest = new CredentialsRequest();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }

        LoginFragmentBinding loginFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.login_fragment,
                container, false);
        View mainView = loginFragmentBinding.getRoot();
        loginFragmentBinding.setCredentialsRequest(this.credentialsRequest);
        ButterKnife.bind(this, mainView);

        CourierApplication.getClientsComponent().inject(this);
        return mainView;
    }


    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @OnClick(R.id.login_button)
    void login() {
        if (PasswordValidator.valid(this.credentialsRequest)) {
            Disposable disposable = this.loginClient.login(this.credentialsRequest)
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
                        }
//                   ((MainActivity) getActivity()).setBaseForBackStack(new MainViewPagerFragment());
                    }, (Throwable e) -> {
                        if (e instanceof LoginException) {
                            Log.i(TAG, "LoginException", e);
                            this.errorMessage.setText(getString(R.string.login_error));
                        } else if (e instanceof BadRequestException) {
                            Log.i(TAG, "Server error", e);
                            this.errorMessage.setText(getString(R.string.server_error));
                        }
                    });

            this.compositeDisposable.add(disposable);
        } else {
            this.errorMessage.setText(getString(PasswordValidator.getErrorMessageCode()));
        }
    }
}
