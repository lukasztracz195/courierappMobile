package com.project.sopmmobileapp.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.project.sopmmobileapp.R;
import com.project.sopmmobileapp.applications.VoteApplication;
import com.project.sopmmobileapp.databinding.RegisterLayoutBinding;
import com.project.sopmmobileapp.model.bundlers.ABundler;
import com.project.sopmmobileapp.model.di.clients.RegisterClient;
import com.project.sopmmobileapp.model.dtos.request.RegisterCredentialsRequest;
import com.project.sopmmobileapp.model.dtos.response.BaseResponse;
import com.project.sopmmobileapp.model.exceptions.BadRequestException;
import com.project.sopmmobileapp.model.exceptions.UserIsTakenException;
import com.project.sopmmobileapp.model.validators.PasswordValidator;
import com.project.sopmmobileapp.view.activities.MainActivity;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RegisterFragment extends Fragment {

    private final static String TAG = RegisterFragment.class.getName();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static String  REGISTER_SUCCESSFUL_MESSAGE ="Register is successful";
    private static String  USER_TAKEN_MESSAGE ="User is taken";

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

        RegisterLayoutBinding registerLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.register_layout,
                container, false);
        registerLayoutBinding.setRegisterCredentialsRequest(this.registerCredentialsRequest);
        View mainView = registerLayoutBinding.getRoot();
        ButterKnife.bind(this, mainView);
        VoteApplication.getClientsComponent().inject(this);

        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (registerCredentialsRequest != null) {
            Icepick.saveInstanceState(this.registerCredentialsRequest, outState);
        }
    }

    @OnClick(R.id.sign_button)
    void register() {
        if (PasswordValidator.valid(this.registerCredentialsRequest)) {
            Disposable disposable = this.registerClient.register(PasswordValidator.toCredential(this.registerCredentialsRequest))
                    .subscribe((BaseResponse authenticationResponse) -> {
                        Log.i(TAG, "Logged in");
//                    TokenStore.saveToken(authenticationResponse.getToken());
//                    CredentialsStore.saveCredentials(this.credentialsRequest);
                        Toast.makeText(this.getContext(),REGISTER_SUCCESSFUL_MESSAGE,
                                Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).changeFragment(new LoginFragment());
                    }, (Throwable e) -> {
                        if (e instanceof UserIsTakenException) {
                            Log.i(TAG, "User is taken", e);;
                            this.errorMessage.setText(getString(R.string.user_is_taken));
                        } else if(e instanceof BadRequestException){
                            Log.i(TAG, "Server error", e);
                            this.errorMessage.setText(getString(R.string.server_error));
                        }
                    });

            this.compositeDisposable.add(disposable);
        }else{
            this.errorMessage.setText(getString(PasswordValidator.getErrorMessageCode()));
        }
    }
}
