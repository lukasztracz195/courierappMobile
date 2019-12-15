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
import androidx.fragment.app.Fragment;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.RegisterWorkerFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.di.clients.RegisterClient;
import com.project.courierapp.model.dtos.request.RegisterCredentialsRequest;
import com.project.courierapp.model.exceptions.http.BadRequestException;
import com.project.courierapp.model.exceptions.LoginException;
import com.project.courierapp.model.validators.RegisterValidator;
import com.project.courierapp.view.Iback.BackWithRemoveFromStack;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.fragments.BaseFragmentTags;
import com.project.courierapp.view.fragments.base_layer.ManagerBaseFragment;
import com.project.courierapp.view.fragments.manager_layer.ManagerFragmentTags;
import com.project.courierapp.view.toasts.ToastFactory;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RegisterWorkerFragment extends Fragment implements BackWithRemoveFromStack {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.error_message)
    TextView errorMessage;

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
        View mainView = registerWorkerFragmentBinding.getRoot();
        registerWorkerFragmentBinding.setRegisterCredentialsRequest(registerCredentialsRequest);
        ButterKnife.bind(this, mainView);

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
            errorMessage.setText(registerValidator.getErrorMessages().toString());
        } else {
            Disposable disposable = registerClient.register(registerCredentialsRequest)
                    .subscribe(request -> {
                            Log.i(ManagerFragmentTags.RegisterWorkerFragment,
                                    "New worker was register");
                            ToastFactory.createToast(Objects.requireNonNull(getContext()),
                                    "New Worker was register");
                    }, (Throwable e) -> {
                        if (e instanceof LoginException) {
                            Log.i(ManagerFragmentTags.RegisterWorkerFragment,
                                    "RegisterException", e);
                            this.errorMessage.setText(getString(R.string.login_error));
                        } else if (e instanceof BadRequestException) {
                            Log.i(ManagerFragmentTags.RegisterWorkerFragment,
                                    "Server error", e);
                            this.errorMessage.setText(getString(R.string.server_error));
                        }
                    });
            compositeDisposable.add(disposable);
        }
        if(errorMessage.getText().toString().isEmpty()){
            MainActivity.instance
                    .setBaseForBackStack(new ManagerBaseFragment(),
                            BaseFragmentTags.ManagerBaseFragment);
        }
    }
}
