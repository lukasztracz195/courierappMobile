package com.project.courierapp.view.fragments.base_layer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.TextInputEditText;
import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.LoginFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.constans.Roles;
import com.project.courierapp.model.deserializers.JwtDeserializer;
import com.project.courierapp.model.di.clients.LoginClient;
import com.project.courierapp.model.di.clients.WorkerClient;
import com.project.courierapp.model.dtos.request.CredentialsRequest;
import com.project.courierapp.model.enums.Role;
import com.project.courierapp.model.interceptors.LoginInterceptor;
import com.project.courierapp.model.store.CredentialsStore;
import com.project.courierapp.model.store.RolesStore;
import com.project.courierapp.model.store.TokenStore;
import com.project.courierapp.model.validators.CredentialsValidator;
import com.project.courierapp.model.validators.TextValidator;
import com.project.courierapp.model.validators.components.WhiteCharsValidatorChain;
import com.project.courierapp.model.watchers.WatcherEditText;
import com.project.courierapp.view.Iback.BackWithExitDialog;
import com.project.courierapp.view.fragments.BaseFragment;
import com.project.courierapp.view.fragments.BaseFragmentTags;
import com.project.courierapp.view.toasts.ToastFactory;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LoginFragment extends BaseFragment implements BackWithExitDialog {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.error_message)
    TextView errorMessage;

    @BindView(R.id.usernameTextInputEditText)
    TextInputEditText usernameTextInputEditText;

    @BindView(R.id.passwordTextInputEditText)
    TextInputEditText passwordTextInputEditText;

    @BindView(R.id.login_button)
    Button loginButton;

    @Inject
    LoginClient loginClient;

    @Inject
    WorkerClient workerClient;

    @State(ABundler.class)
    CredentialsRequest credentialsRequest = new CredentialsRequest();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }

        LoginFragmentBinding loginFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.login_fragment,
                container, false);
        mainView = loginFragmentBinding.getRoot();
        loginFragmentBinding.setCredentialsRequest(this.credentialsRequest);
        ButterKnife.bind(this, mainView);
        setValidators();
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
        CredentialsValidator credentialsValidator = CredentialsValidator.of(credentialsRequest);
        credentialsValidator.validate();
        if(credentialsValidator.isValid()) {
            loginButton.setEnabled(false);
            Disposable disposable = loginClient.login(credentialsRequest)
                    .subscribe(token -> {
                                Log.i(BaseFragmentTags.LoginFragment, "Logged in");
                                TokenStore.saveToken(token);
                                CredentialsStore.saveCredentials(credentialsRequest);
                                Role role = getRoleFromTokenStore();
                                switch (role) {
                                    case MANAGER:
                                        switchOnManagerBaseFragment();
                                        break;
                                    case WORKER:
                                        checkWorkingStatusAndSwitchOnWorkerFragment();
                                        break;
                                    case TEMPORARY:
                                        switchOnChangePasswordFragment();
                                        break;
                                }
                            }, (Throwable e) -> {
                                LoginInterceptor.of(errorMessage).getError(e);
                                loginButton.setEnabled(true);
                            }
                    );
            this.compositeDisposable.add(disposable);
        }else{
            errorMessage.setText(credentialsValidator.getErrorMessage());
            ToastFactory.createToast(Objects.requireNonNull(getContext()),
                    credentialsValidator.getErrorMessage());
        }

    }

    @OnClick(R.id.usernameTextInputEditText)
    public void clearErrorAfterUsernameInputEditTextUsed() {
        clearErrorMessage();
    }

    @OnClick(R.id.passwordTextInputEditText)
    public void clearErrorAfterPasswordInputEditTextUsed() {
        clearErrorMessage();
    }

    private void clearErrorMessage() {
        errorMessage.setText("");
    }

    private void setValidators() {
        usernameTextInputEditText.addTextChangedListener(WatcherEditText.of(
                usernameTextInputEditText,
                errorMessage,
                TextValidator.of(Collections.singletonList(
                        WhiteCharsValidatorChain.of(
                                Objects.requireNonNull(
                                        usernameTextInputEditText.getText()
                                ).toString())
                ))));
        passwordTextInputEditText.addTextChangedListener(WatcherEditText.of(
                passwordTextInputEditText,
                errorMessage,
                TextValidator.of(Collections.singletonList(
                        WhiteCharsValidatorChain.of(
                                Objects.requireNonNull(
                                        passwordTextInputEditText.getText()
                                ).toString())
                ))));
    }

    private void checkWorkingStatusAndSwitchOnWorkerFragment() {
        Log.i(BaseFragmentTags.LoginFragment, Roles.WORKER);
        RolesStore.saveRole(Roles.WORKER);
        Disposable disposable = workerClient.isBusy().subscribe(worker -> {
            if (worker.isBusy()) {
                activity.putFragment(new WorkerBaseFragment(true),
                        BaseFragmentTags.WorkerBaseFragment);
            } else {
                activity.putFragment(new WorkerBaseFragment(false),
                        BaseFragmentTags.WorkerBaseFragment);
            }
        }, (Throwable e) -> {
            errorMessage.setText(e.getMessage());
        });
        this.compositeDisposable.add(disposable);
    }

    private void switchOnChangePasswordFragment() {
        Log.i(BaseFragmentTags.LoginFragment, Roles.TEMPORARY);
        RolesStore.saveRole(Roles.TEMPORARY);
        activity.putFragment(new ChangePasswordFragment(),
                BaseFragmentTags.ChangePasswordFragment);
    }

    private void switchOnManagerBaseFragment() {
        Log.i(BaseFragmentTags.LoginFragment, Roles.MANAGER);
        RolesStore.saveRole(Roles.MANAGER);
        activity.setBaseForBackStack(new ManagerBaseFragment(),
                BaseFragmentTags.ManagerBaseFragment);
    }

    private Role getRoleFromTokenStore() {
        Map<String, String> map = JwtDeserializer.decoded(TokenStore.getToken());
        String role = map.get("\"roles\"");
        if (Objects.requireNonNull(role).contains(Roles.MANAGER)) {
            return Role.MANAGER;
        } else if (role.contains(Roles.WORKER)) {
            return Role.WORKER;
        }
        return Role.TEMPORARY;
    }
}
