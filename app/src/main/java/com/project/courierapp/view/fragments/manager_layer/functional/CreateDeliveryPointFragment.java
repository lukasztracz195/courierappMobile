package com.project.courierapp.view.fragments.manager_layer.functional;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.project.courierapp.databinding.CreateDeliveryPointFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.di.clients.DeliveryPointsClient;
import com.project.courierapp.model.dtos.request.AddOrEditDeliveryPointRequest;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.transfer.DeliveryPointDto;
import com.project.courierapp.model.validators.TextValidator;
import com.project.courierapp.model.validators.ValidatorBuilder;
import com.project.courierapp.model.validators.components.EmptyFieldsValidatorChain;
import com.project.courierapp.model.watchers.WatcherEditText;
import com.project.courierapp.view.Iback.BackWithRemoveFromStack;
import com.project.courierapp.view.Iback.IOnBackPressed;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.fragments.BaseFragment;
import com.project.courierapp.view.fragments.manager_layer.ManagerFragmentTags;
import com.project.courierapp.view.toasts.ToastFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lombok.Setter;

@Setter
public class CreateDeliveryPointFragment extends BaseFragment implements BackWithRemoveFromStack, IOnBackPressed {

    private List<DeliveryPointResponse> deliveryPointResponseList = new ArrayList<>();

    @BindView(R.id.error_message)
    TextView errorMessage;

    @BindView(R.id.address_content)
    TextInputEditText addresInputEditText;

    @BindView(R.id.postal_code_content)
    TextInputEditText postalCodeInputEditText;

    @BindView(R.id.city_content)
    TextInputEditText cityInputEditText;

    @BindView(R.id.country_content)
    TextInputEditText countryInputEditText;

    @State(ABundler.class)
    DeliveryPointDto deliveryPointDto = new DeliveryPointDto();

    @Inject
    DeliveryPointsClient deliveryPointsClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CreateDeliveryPointFragment() {
    }

    public CreateDeliveryPointFragment(List<DeliveryPointResponse> deliveryPointResponseList) {
        this.deliveryPointResponseList = deliveryPointResponseList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        CreateDeliveryPointFragmentBinding createDeliveryPointFragmentBinding = DataBindingUtil
                .inflate(inflater,
                        R.layout.create_delivery_point_fragment, container, false);

        mainView = createDeliveryPointFragmentBinding.getRoot();
        createDeliveryPointFragmentBinding.setDeliveryPointDto(deliveryPointDto);
        ButterKnife.bind(this, mainView);
        CourierApplication.getClientsComponent().inject(this);
        setValidators();
        return mainView;
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void setValidators() {
        List<TextInputEditText> textInputEditTexts = Arrays.asList(addresInputEditText,
                postalCodeInputEditText, cityInputEditText, countryInputEditText);
        for (TextInputEditText textInputEditText : textInputEditTexts) {
            textInputEditText.addTextChangedListener(WatcherEditText.of(textInputEditText,
                    errorMessage, new TextValidator()));
        }
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.create_delivery_point)
    public void save() {
        ValidatorBuilder validatorBuilder = createDeliveryPointsValidator();
        if (validatorBuilder.isValid()) {
            saveDeliveryPoint();
        } else {
            showErrorMessage(validatorBuilder);
        }
    }

    private void moveToCreateRoadFragment(DeliveryPointResponse deliveryPointResponse) {
        deliveryPointResponseList.add(deliveryPointResponse);
        ((MainActivity) Objects.requireNonNull(getActivity()))
                .putFragment(new CreateRoadFragment(deliveryPointResponseList),
                        ManagerFragmentTags.CreateRoadFragment);
    }

    @OnClick(R.id.cancel_bt)
    public void cancel() {
        ((MainActivity) Objects.requireNonNull(getActivity()))
                .putFragment(new CreateRoadFragment(deliveryPointResponseList),
                        ManagerFragmentTags.CreateRoadFragment);
    }

    private ValidatorBuilder createDeliveryPointsValidator() {
        return ValidatorBuilder.builder()
                .add(
                        EmptyFieldsValidatorChain.of(
                                Arrays.asList(
                                        deliveryPointDto.getAddress(),
                                        deliveryPointDto.getPostalCode(),
                                        deliveryPointDto.getCity(),
                                        deliveryPointDto.getCountry()))
                )
                .validate();
    }

    private void saveDeliveryPoint() {
        Disposable disposable = deliveryPointsClient.
                addDeliveryPoint(AddOrEditDeliveryPointRequest.of(deliveryPointDto))
                .subscribe(this::moveToCreateRoadFragment, e ->
                        errorMessage.setText(e.getMessage()));
        compositeDisposable.add(disposable);
    }

    private void showErrorMessage(ValidatorBuilder validatorBuilder) {
        errorMessage.setText(validatorBuilder.getErrorMessage());
        ToastFactory.createToast(Objects.requireNonNull(getContext()),
                validatorBuilder.getErrorMessage());
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
