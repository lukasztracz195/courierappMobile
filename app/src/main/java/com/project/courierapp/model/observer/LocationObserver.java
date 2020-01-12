package com.project.courierapp.model.observer;

import android.location.Location;

public interface LocationObserver {

    void notifyAll(Location location);
}
