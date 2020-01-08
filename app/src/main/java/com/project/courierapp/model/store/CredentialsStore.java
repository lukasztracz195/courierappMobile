package com.project.courierapp.model.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;
import com.project.courierapp.model.dtos.request.CredentialsRequest;

public class CredentialsStore {

    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    private static final SharedPreferences sharedPreferences;

    static {
        Context context = CourierApplication.getContext();
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.credentials_preferences), Context.MODE_PRIVATE);
    }

    public static void saveCredentials(CredentialsRequest credentialsRequest) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME_KEY, credentialsRequest.getUsername());
        editor.putString(PASSWORD_KEY, credentialsRequest.getPassword());
        editor.apply();
    }

    public static String getUsername() {
        return sharedPreferences.getString(USERNAME_KEY, "");
    }

    public static String getPassword() {
        return sharedPreferences.getString(PASSWORD_KEY, "");
    }

    public static void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USERNAME_KEY);
        editor.remove(PASSWORD_KEY);
        editor.apply();
    }
}
