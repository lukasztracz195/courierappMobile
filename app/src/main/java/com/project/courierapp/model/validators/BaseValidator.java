package com.project.courierapp.model.validators;

import lombok.Setter;

@Setter
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
    public boolean isInvalid() {
      return validatorBuilder.isInvalid();
    }

    @Override
    public String getErrorMessage() {
        return validatorBuilder.getErrorMessage();
    }
}
