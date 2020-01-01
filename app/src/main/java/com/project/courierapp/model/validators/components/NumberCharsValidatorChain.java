package com.project.courierapp.model.validators.components;

import android.util.Pair;

public class NumberCharsValidatorChain implements ValidatorChain {

    private static final String ERROR_MESSAGE_TO_LESS_CHARS = "Text cannot contains less chars than: ";
    private static final String ERROR_MESSAGE_TO_MORE_CHARS = "Text cannot contains more chars than: ";
    private String textToValidation;
    private int minNumberChars= 0;
    private int maxNumberChars= 0;

    private NumberCharsValidatorChain(String textToValidation, int minNumberChars, int maxNumberChars) {
        this.textToValidation = textToValidation;
        this.minNumberChars = minNumberChars;
        this.maxNumberChars = maxNumberChars;
    }

    public static NumberCharsValidatorChain of(String textToValidation, int minNumberChars, int maxNumberChars) {
        return new NumberCharsValidatorChain(textToValidation, minNumberChars, maxNumberChars);
    }

    @Override
    public Pair<Boolean, String> validate() {

        int numbersChars = textToValidation.length();
        if(numbersChars < minNumberChars){
            return Pair.create(false, ERROR_MESSAGE_TO_LESS_CHARS + minNumberChars);
        }
        if(numbersChars > maxNumberChars){
            return Pair.create(false, ERROR_MESSAGE_TO_MORE_CHARS + maxNumberChars);
        }
        return Pair.create(true, VALIDATION_PASSED);
    }

    @Override
    public void setTextToValidation(String textWithProbableWhiteChars) {
        this.textToValidation = textWithProbableWhiteChars;
    }
}
