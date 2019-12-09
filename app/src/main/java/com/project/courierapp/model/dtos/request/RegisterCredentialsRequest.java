package com.project.courierapp.model.dtos.request;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.NoArgsConstructor;

@Parcel
@NoArgsConstructor
public class RegisterCredentialsRequest extends BaseObservable {

    @SerializedName("login")
    String login;

    @SerializedName("email")
    String email;



    @Bindable
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.password);
    }
}
