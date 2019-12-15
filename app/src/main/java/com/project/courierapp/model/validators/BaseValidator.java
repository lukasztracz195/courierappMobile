package com.project.courierapp.model.validators;

import java.util.List;

public class BaseValidator implements Validator {

    protected ValidatorBuilder validatorBuilder;
    @Override
    public void validate() {

    }

    @Override
    public boolean isValid() {
        return validatorBuilder.isValid();
    }

    @Override
    public List<String> getErrorMessages() {
        return validatorBuilder.getErrorsMessages();
    }
}
