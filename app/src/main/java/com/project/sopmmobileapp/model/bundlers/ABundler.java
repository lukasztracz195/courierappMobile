package com.project.sopmmobileapp.model.bundlers;

import android.os.Bundle;

import org.parceler.Parcels;

import icepick.Bundler;

public class ABundler implements Bundler<Object> {

    @Override
    public void put(String s, Object object, Bundle bundle) {
        bundle.putParcelable(s, Parcels.wrap(object));
    }

    @Override
    public Object get(String s, Bundle bundle) {
        return Parcels.unwrap(bundle.getParcelable(s));
    }
}
