package com.project.sopmmobileapp.model.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.sopmmobileapp.R;
import com.project.sopmmobileapp.applications.VoteApplication;
import com.project.sopmmobileapp.model.dtos.request.CredentialsRequest;

public class CredentialsStore {

    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    private static final SharedPreferences sharedPreferences;

    static {
        Context context = VoteApplication.getContext();
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
}
