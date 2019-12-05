package com.project.courierapp.view.fragments.base_layer;

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
import com.project.courierapp.databinding.LoginFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.constans.Roles;
import com.project.courierapp.model.deserializers.JwtDeserializer;
import com.project.courierapp.model.di.clients.LoginClient;
import com.project.courierapp.model.dtos.request.CredentialsRequest;
import com.project.courierapp.model.exceptions.BadRequestException;
import com.project.courierapp.model.exceptions.LoginException;
import com.project.courierapp.model.store.CredentialsStore;
import com.project.courierapp.model.store.RolesStore;
import com.project.courierapp.model.store.TokenStore;
import com.project.courierapp.model.validators.PasswordValidator;
import com.project.courierapp.view.Iback.BackWithExitDialog;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.fragments.BaseFragmentTags;

import org.jetbrains.annotations.NotNull;

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

public class LoginFragment extends Fragment implements BackWithExitDialog {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.error_message)
    TextView errorMessage;

    @Inject
    LoginClient loginClient;


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
                        Log.i(BaseFragmentTags.LoginFragment, "Logged in");
                        TokenStore.saveToken(token);
                        CredentialsStore.saveCredentials(this.credentialsRequest);
                        Map<String, String> map = JwtDeserializer.decoded(TokenStore.getToken());
                        String role = map.get("\"roles\"");
                        if (Objects.requireNonNull(role).contains(Roles.MANAGER)) {
                            Log.i(BaseFragmentTags.LoginFragment, Roles.MANAGER);
                            RolesStore.saveRole(Roles.MANAGER);
                            ((MainActivity) Objects.requireNonNull(getActivity()))
                                    .putFragment(new ManagerBaseFragment(),
                                            BaseFragmentTags.ManagerBaseFragment);
                        } else if (role.contains(Roles.WORKER)) {
                            Log.i(BaseFragmentTags.LoginFragment, Roles.WORKER);
                            RolesStore.saveRole(Roles.WORKER);
                        } else {
                            Log.i(BaseFragmentTags.LoginFragment, Roles.TEMPORARY);
                            RolesStore.saveRole(Roles.TEMPORARY);
                            ((MainActivity) Objects.requireNonNull(getActivity()))
                                    .putFragment(new ChangePasswordFragment(),
                                            BaseFragmentTags.ChangePasswordFragment);
                        }

                    }, (Throwable e) -> {
                        if (e instanceof LoginException) {
                            Log.i(BaseFragmentTags.LoginFragment, "LoginException", e);
                            this.errorMessage.setText(getString(R.string.login_error));
                        } else if (e instanceof BadRequestException) {
                            Log.i(BaseFragmentTags.LoginFragment, "Server error", e);
                            this.errorMessage.setText(getString(R.string.server_error));
                        }
                    });

            this.compositeDisposable.add(disposable);
        } else {
            this.errorMessage.setText(getString(PasswordValidator.getErrorMessageCode()));
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
}
