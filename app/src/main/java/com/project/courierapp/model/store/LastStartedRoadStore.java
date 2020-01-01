package com.project.courierapp.model.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.courierapp.R;
import com.project.courierapp.applications.CourierApplication;

public class LastStartedRoadStore {

    private static final String LAST_STARTED_ROAD_ID = "lastStartedRoadId";

    private static final SharedPreferences sharedPreferences;

    static {
        Context context = CourierApplication.getContext();
        sharedPreferences = context.getSharedPreferences(context
                .getString(R.string.user_mode), Context.MODE_PRIVATE);
    }

    public static void saveRoadId(Long roadId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_STARTED_ROAD_ID, roadId);
        editor.apply();
    }

    public static Long getLastStartedRoadId() {
        return sharedPreferences.getLong(LAST_STARTED_ROAD_ID,0);
    }

    public static void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(LAST_STARTED_ROAD_ID);
        editor.apply();
    }
}
