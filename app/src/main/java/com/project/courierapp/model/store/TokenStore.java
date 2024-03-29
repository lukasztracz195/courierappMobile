package com.project.courierapp.model.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;

public class TokenStore {
    private static final String TOKEN_KEY = "tokenKey";

    private static final SharedPreferences sharedPreferences;

    static {
        Context context = CourierApplication.getContext();
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.auth_preferences), Context.MODE_PRIVATE);
    }

    public static void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public static String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, "");
    }

    public static void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();
    }
}
