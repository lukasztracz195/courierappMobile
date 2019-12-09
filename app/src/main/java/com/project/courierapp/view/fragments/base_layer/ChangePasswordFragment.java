package com.project.courierapp.view.fragments.base_layer;

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

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.ChangePasswordFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.di.clients.ChangePasswordClient;
import com.project.courierapp.model.dtos.request.ChangePasswordRequest;
import com.project.courierapp.model.dtos.transfer.ChangePasswordDto;
import com.project.courierapp.model.exceptions.BadRequestException;
import com.project.courierapp.model.exceptions.ChangePasswordException;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.fragments.BaseFragmentTags;

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


public class ChangePasswordFragment extends Fragment{

    @BindView(R.id.error_message)
    TextView errorMessage;

    @Inject
    ChangePasswordClient changePasswordClient;

    @State(ABundler.class)
    ChangePasswordDto changePasswordDto = new ChangePasswordDto();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        changePasswordFragmentBinding.setChangePasswordDto(this.changePasswordDto);
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
    public void changePassword() {
        Disposable disposable = changePasswordClient.changePassword(changePasswordDto.getUsername(),
                ChangePasswordRequest.builder()
                        .oldPassword(changePasswordDto.getOldPassword())
                        .newPassword(changePasswordDto.getNewPassword())
                        .build())
                .subscribe(token -> {
                    Log.i(BaseFragmentTags.ChangePasswordFragment, "Changed Password in");
                    Toast.makeText(getContext(), getResources().getText(R.string.toast_change_password),
                            Toast.LENGTH_SHORT).show();
                    ((MainActivity) Objects.requireNonNull(getActivity()))
                            .putFragment(new LoginFragment(), BaseFragmentTags.LoginFragment);
                }, (Throwable e) ->
                {
                    if (e instanceof ChangePasswordException) {
                        Log.i(BaseFragmentTags.ChangePasswordFragment, "ChangePasswordException", e);
                        this.errorMessage.setText(getString(R.string.login_error));
                    } else if (e instanceof BadRequestException) {
                        Log.i(BaseFragmentTags.ChangePasswordFragment, "Server error", e);
                        this.errorMessage.setText(getString(R.string.server_error));
                    }
                });
        this.compositeDisposable.add(disposable);
        if(errorMessage.getText().toString().isEmpty()){
            Log.i(BaseFragmentTags.ChangePasswordFragment, "Changed Password in");
            Toast.makeText(getContext(), getResources().getText(R.string.toast_change_password),
                    Toast.LENGTH_SHORT).show();
            ((MainActivity) Objects.requireNonNull(getActivity()))
                    .setBaseForBackStack(new LoginFragment(), BaseFragmentTags.LoginFragment);
        }
    }
}
