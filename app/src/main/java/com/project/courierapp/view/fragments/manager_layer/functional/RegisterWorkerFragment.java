package com.project.courierapp.view.fragments.manager_layer.functional;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.TextInputEditText;
import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.RegisterWorkerFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.di.clients.RegisterClient;
import com.project.courierapp.model.dtos.request.RegisterCredentialsRequest;
import com.project.courierapp.model.interceptors.CreateWorkerInterceptor;
import com.project.courierapp.model.validators.RegisterValidator;
import com.project.courierapp.model.validators.TextValidator;
import com.project.courierapp.model.validators.components.EmailValidatorChain;
import com.project.courierapp.model.watchers.WatcherEditText;
import com.project.courierapp.view.Iback.BackWithRemoveFromStack;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.fragments.BaseFragment;
import com.project.courierapp.view.fragments.BaseFragmentTags;
import com.project.courierapp.view.fragments.base_layer.ManagerBaseFragment;
import com.project.courierapp.view.fragments.manager_layer.ManagerFragmentTags;
import com.project.courierapp.view.toasts.ToastFactory;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RegisterWorkerFragment extends BaseFragment implements BackWithRemoveFromStack {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.error_message)
    TextView errorMessage;

    @BindView(R.id.emailTextInputEditText)
    TextInputEditText emailTextInputEditText;

    @BindView(R.id.usernameTextInputEditText)
    TextInputEditText usernameTextInputEditText;

    @Inject
    RegisterClient registerClient;

    @State(ABundler.class)
    RegisterCredentialsRequest registerCredentialsRequest = new RegisterCredentialsRequest();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        RegisterWorkerFragmentBinding registerWorkerFragmentBinding = DataBindingUtil
                .inflate(inflater, R.layout.register_worker_fragment,
                        container, false);
        mainView = registerWorkerFragmentBinding.getRoot();
        registerWorkerFragmentBinding.setRegisterCredentialsRequest(registerCredentialsRequest);
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

    @OnClick(R.id.register_button)
    void register() {
        RegisterValidator registerValidator = RegisterValidator.of(registerCredentialsRequest);
        registerValidator.validate();
        if (!registerValidator.isValid()) {
            errorMessage.setText(registerValidator.getErrorMessage());
        } else {
            Disposable disposable = registerClient.register(registerCredentialsRequest)
                    .subscribe(request -> {
                        Log.i(ManagerFragmentTags.RegisterWorkerFragment,
                                "New worker was register");
                        ToastFactory.createToast(Objects.requireNonNull(getContext()),
                                "New Worker was register");
                    }, (Throwable e) -> {
                        CreateWorkerInterceptor.of(errorMessage, e);
                    });
            compositeDisposable.add(disposable);
        }
        if (errorMessage.getText().toString().isEmpty()) {
            MainActivity.instance
                    .setBaseForBackStack(new ManagerBaseFragment(),
                            BaseFragmentTags.ManagerBaseFragment);
        }
    }

    private void setValidators() {
        usernameTextInputEditText.addTextChangedListener(WatcherEditText.of(
                usernameTextInputEditText, errorMessage, new TextValidator()));
        emailTextInputEditText.addTextChangedListener(WatcherEditText.of(
                emailTextInputEditText,
                errorMessage,
                TextValidator.of(Collections.singletonList(
                        EmailValidatorChain.of(
                                Objects.requireNonNull(emailTextInputEditText.getText()).toString())
                ))));
    }
}
