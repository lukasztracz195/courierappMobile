package com.project.courierapp.model.validators.components;

import android.util.Pair;

public interface ValidatorChain {

    String VALIDATION_PASSED = "Validation passed";

    Pair<Boolean, String> validate();

}
