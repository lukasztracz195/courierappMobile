package com.project.courierapp.model.dtos.transfer;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.project.courierapp.model.dtos.request.Request;
import com.project.courierapp.model.dtos.response.DeliveryPointResponse;

import org.parceler.Parcel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Builder
@Parcel
@Getter
@Setter
public class DeliveryPointDto extends BaseObservable implements Request, Serializable {

    boolean editabled;
    boolean saved;
    String address;
    String postalCode;
    String city;
    String country;
    String expendedTime;
    Long pointId;

    public DeliveryPointDto() {
        editabled = false;
        saved = false;
        address = "";
        postalCode = "";
        city = "";
        country = "";
        expendedTime = "0";
        pointId = null;
    }

    public DeliveryPointDto(DeliveryPointResponse deliveryPointResponse) {
        String[] fields = deliveryPointResponse.getAddress().split(",");

        if (fields.length == 3) {
            editabled = false;
            saved = true;
            address = fields[0].trim();
            postalCode = fields[1].trim().split(" ")[0].trim();
            city = fields[1].trim().split(" ")[1].trim();
            country = fields[2].trim();
            expendedTime = deliveryPointResponse.getExpectedSpendTime();
            pointId = deliveryPointResponse.getPointId();
        }
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
        return expendedTime;
    }

    public void setExpendedTime(String expendedTime) {
        if (!expendedTime.isEmpty()) {
            this.expendedTime = expendedTime;
        } else {
            this.expendedTime = "0";
        }
        notifyPropertyChanged(BR.expendedTime);
    }
}
