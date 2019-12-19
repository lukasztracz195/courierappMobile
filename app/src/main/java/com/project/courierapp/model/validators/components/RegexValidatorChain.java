package com.project.courierapp.model.validators.components;

import android.util.Pair;

public class RegexValidatorChain implements ValidatorChain {

    private String errorMessage;
    private String regex;
    private String textToValidation;

    private RegexValidatorChain(String textToValidation, String regex, String errorMessage) {
        this.textToValidation = textToValidation;
        this.regex = regex;
        this.errorMessage = errorMessage;
    }

    private RegexValidatorChain(String regex, String errorMessage) {
        this.regex = regex;
        this.errorMessage = errorMessage;
    }


    public static RegexValidatorChain of(String textToValidation, String regex, String errorMessage) {
        return new RegexValidatorChain(textToValidation, regex, errorMessage);
    }

    public RegexValidatorChain of(String regex, String errorMessage) {
        return new RegexValidatorChain(regex, errorMessage);
    }

    @Override
    public Pair<Boolean, String> validate() {
        boolean matchesResult = textToValidation.matches(regex);
        if (matchesResult) {
            return new Pair<>(true, ValidatorChain.VALIDATION_PASSED);
        }
        return new Pair<>(false, errorMessage);
    }

    @Override
    public void setTextToValidation(String textToValidation) {
        this.textToValidation = textToValidation;
    }
}
