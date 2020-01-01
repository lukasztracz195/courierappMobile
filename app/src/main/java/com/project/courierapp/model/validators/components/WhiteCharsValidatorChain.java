package com.project.courierapp.model.validators.components;

import android.util.Pair;

public class WhiteCharsValidatorChain implements ValidatorChain {

    private static final String ERROR_MESSAGE = "Text cannot contains white chars";
    private String textToValidation;

    private WhiteCharsValidatorChain(String textToValidation) {
        this.textToValidation = textToValidation;
    }

    public static WhiteCharsValidatorChain of(String textToValidation) {
        return new WhiteCharsValidatorChain(textToValidation);
    }

    @Override
    public Pair<Boolean, String> validate() {

        if(textToValidation.contains(" ")){
            return Pair.create(false, ERROR_MESSAGE);
        }
        return Pair.create(true,VALIDATION_PASSED);
    }

    @Override
    public void setTextToValidation(String textWithProbableWhiteChars) {
        this.textToValidation = textWithProbableWhiteChars;
    }
}
