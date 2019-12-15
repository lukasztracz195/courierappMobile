package com.project.courierapp.model.validators;

import android.util.Pair;

import com.project.courierapp.model.validators.components.ValidatorChain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidatorBuilder {

    private Set<Boolean> validateSet = new HashSet<>();
    private List<String> errorMessages = new ArrayList<>();
    private List<ValidatorChain> validatorChains = new ArrayList<>();


    public static ValidatorBuilder builder() {
        return new ValidatorBuilder();
    }

    public ValidatorBuilder add(ValidatorChain validatorChain) {
        validatorChains.add(validatorChain);
        return this;
    }

    public ValidatorBuilder validate(){
        for(ValidatorChain validatorChain : validatorChains){
            Pair<Boolean, String> validationResponse = validatorChain.validate();
            validateSet.add(validationResponse.first);
            if(!validationResponse.second.equals(ValidatorChain.VALIDATION_PASSED)) {
                errorMessages.add(validationResponse.second);
            }
        }
        return  this;
    }

    public boolean isValid() {
        return validateSet.contains(false);
    }

    public List<String> getErrorsMessages(){
        return errorMessages;
    }
}
