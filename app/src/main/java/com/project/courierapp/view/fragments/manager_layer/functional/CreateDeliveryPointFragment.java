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
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.databinding.CreateDeliveryPointFragmentBinding;
import com.project.courierapp.model.bundlers.ABundler;
import com.project.courierapp.model.di.clients.DeliveryPointsClient;
import com.project.courierapp.model.dtos.request.AddDeliveryPointRequest;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;
import com.project.courierapp.model.dtos.transfer.DeliveryPointDto;
import com.project.courierapp.model.watchers.WatcherEditText;
import com.project.courierapp.view.Iback.BackWithRemoveFromStack;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.fragments.manager_layer.ManagerFragmentTags;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import lombok.Setter;

@Setter
public class CreateDeliveryPointFragment extends Fragment implements BackWithRemoveFromStack {


    private List<DeliveryPointResponse> deliveryPointResponseList;

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
        CreateDeliveryPointFragmentBinding createDeliveryPointFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.create_delivery_point_fragment, container, false);

        View mainView = createDeliveryPointFragmentBinding.getRoot();
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
                postalCodeInputEditText,cityInputEditText,countryInputEditText);
        addresInputEditText.addTextChangedListener(WatcherEditText.of(addresInputEditText,
                errorMessage,null));
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.create_delivery_point)
    public void save() {
        deliveryPointsClient.addDeliveryPoint(AddDeliveryPointRequest.of(deliveryPointDto))
                .subscribe(response -> {
                    deliveryPointResponseList.add(response);
                    ((MainActivity) Objects.requireNonNull(getActivity()))
                            .putFragment(new CreateRoadFragment(deliveryPointResponseList),
                            ManagerFragmentTags.CreateRoadFragment);
                }, (Throwable e) -> {
                    errorMessage.setText(e.getMessage());
                });
    }

    @OnClick(R.id.cancel_bt)
    public void cancel() {
        //TODO ALLERT DIALOG ON UNSAVED DATA
        ((MainActivity) Objects.requireNonNull(getActivity()))
                .putFragment(new CreateRoadFragment(deliveryPointResponseList),
                        ManagerFragmentTags.CreateRoadFragment);
    }
}
