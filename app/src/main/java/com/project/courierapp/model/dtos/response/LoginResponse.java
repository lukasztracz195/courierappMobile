package com.project.courierapp.model.dtos.response;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class LoginResponse extends BaseObservable implements Response{


    @SerializedName("username")
    private String username;

    @SerializedName("jwt")
    private String token;

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
