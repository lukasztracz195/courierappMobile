package com.project.courierapp.model.validators.components;

import android.util.Pair;

public class EmailValidatorChain implements ValidatorChain {

    private static final String ERROR_MESSAGE = "Email is wrong";
    private static final String EMAIL_REGEX = "(^[a-zA-Z0-9_.+-]+@[a-zA-Z]{2,8}+\\.[a-zA-Z0-9-.]{2,3}+$)";
    private RegexValidatorChain regexValidatorChain;

    private EmailValidatorChain(RegexValidatorChain regexValidatorChain) {
        this.regexValidatorChain = regexValidatorChain;

    }

    public static EmailValidatorChain of(String emailToValidation) {
        return new EmailValidatorChain(RegexValidatorChain.of(emailToValidation,
                EMAIL_REGEX, ERROR_MESSAGE));
    }

    @Override
    public Pair<Boolean, String> validate() {
       return regexValidatorChain.validate();
    }

    @Override
    public void setTextToValidation(String emailToValidation) {
        this.regexValidatorChain.setTextToValidation(emailToValidation);
    }
}
