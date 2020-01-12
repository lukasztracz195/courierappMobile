package com.project.courierapp.model.observer;

import android.location.Location;

public interface LocationSubscriber {

    void notifyByLocation(Location location);
}
