package com.project.courierapp.model.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;

public class RolesStore {
    private static final String ROLE_KEY = "role";

    private static final SharedPreferences sharedPreferences;

    static {
        Context context = CourierApplication.getContext();
        sharedPreferences = context.getSharedPreferences(context
                .getString(R.string.user_mode), Context.MODE_PRIVATE);
    }

    public static void saveRole(String role) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ROLE_KEY, role);
        editor.apply();
    }

    public static String getRole() {
        return sharedPreferences.getString(ROLE_KEY, "");
    }

    public static void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ROLE_KEY);
        editor.apply();
    }
}
