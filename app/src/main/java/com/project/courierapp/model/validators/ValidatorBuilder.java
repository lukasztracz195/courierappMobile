package com.project.courierapp.model.validators;

import android.util.Pair;

import com.project.courierapp.model.validators.components.ValidatorChain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidatorBuilder {

    private Set<Boolean> validateSet = new HashSet<>();
    private String errorMessage;
    private List<ValidatorChain> validatorChains = new ArrayList<>();


    public static ValidatorBuilder builder() {
        return new ValidatorBuilder();
    }

    public ValidatorBuilder add(ValidatorChain validatorChain) {
        validatorChains.add(validatorChain);
        return this;
    }

    public ValidatorBuilder validate() {
        validateSet.clear();
        Iterator iteratorValidatorsChains = validatorChains.iterator();
        while (!validateSet.contains(false) && iteratorValidatorsChains.hasNext()) {
            ValidatorChain validatorChain = (ValidatorChain) iteratorValidatorsChains.next();
            Pair<Boolean, String> validationResponse = validatorChain.validate();
            validateSet.add(validationResponse.first);
            if (!validationResponse.second.equals(ValidatorChain.VALIDATION_PASSED)) {
                errorMessage = validationResponse.second;
            }
        }
        return this;
    }

    public boolean isValid() {
        return !validateSet.contains(false);
    }

    public boolean isInvalid(){
       return validateSet.contains(false);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
