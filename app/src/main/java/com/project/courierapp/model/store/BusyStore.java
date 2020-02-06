package com.project.courierapp.model.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;

public class BusyStore {

    private static final String IS_BUSY = "isBusy";

    private static final SharedPreferences sharedPreferences;

    static {
        Context context = CourierApplication.getContext();
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.credentials_preferences), Context.MODE_PRIVATE);
    }

    public static void saveBusy(boolean isBusy) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_BUSY, isBusy);
        editor.apply();
    }

    public static boolean isBuys() {
        return sharedPreferences.getBoolean(IS_BUSY, false);
    }
}
