package com.project.courierapp.model.dtos.transfer;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.project.courierapp.model.dtos.request.Request;
import com.project.courierapp.model.watchers.Editabled;

import org.parceler.Parcel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;


@AllArgsConstructor
@Builder
@Parcel
public class DeliveryPointDto extends BaseObservable implements Request, Editabled, Serializable {

    boolean editabled;
    boolean saved;
    String address;
    String postalCode;
    String city;
    String country;
    Long expendedTime;

    public DeliveryPointDto() {
        editabled = false;
        saved = false;
        address = "";
        postalCode = "";
        city = "";
        country = "";
        expendedTime = 0L;
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    @Bindable
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        notifyPropertyChanged(BR.postalCode);
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        notifyPropertyChanged(BR.country);
    }

    @Bindable
    public String getExpendedTime() {
        return expendedTime.toString();
    }

    public void setExpendedTime(String expendedTime) {
        if (!expendedTime.isEmpty()) {
            this.expendedTime = Long.parseLong(expendedTime);
        } else {
            this.expendedTime = 0L;
        }
        notifyPropertyChanged(BR.expendedTime);
    }

    @Override
    public boolean isEditeabled() {
        return editabled;
    }

    @Override
    public void setEditabled(boolean editabled) {
        this.editabled = editabled;
    }

    @Override
    public boolean isSaved() {
        return saved;
    }

    @Override
    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
