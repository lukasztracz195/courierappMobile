package com.project.courierapp.model.dtos.request;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.google.gson.annotations.SerializedName;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RegisterCredentialsRequest extends BaseObservable {

    @SerializedName("username")
    String username;

    @SerializedName("password")
    String password;

    @SerializedName("repeatPassword")
    String repeatPassword;

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
        notifyPropertyChanged(BR.repeatPassword);
    }
}
