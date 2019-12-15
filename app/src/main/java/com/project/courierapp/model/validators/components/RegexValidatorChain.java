package com.project.courierapp.model.validators.components;

import android.util.Pair;

public class RegexValidatorChain implements ValidatorChain {

    private String errorMessage;
    private String regex;
    private String textToValidation;

    public RegexValidatorChain(String textToValidation, String regex, String errorMessage) {
        this.textToValidation = textToValidation;
        this.regex = regex;
        this.errorMessage = errorMessage;
    }

    @Override
    public Pair<Boolean, String> validate() {
        boolean matchesResult = textToValidation.matches(regex);
        if (matchesResult) {
            return new Pair<>(true, ValidatorChain.VALIDATION_PASSED);
        }
        return new Pair<>(false, errorMessage);
    }
}
