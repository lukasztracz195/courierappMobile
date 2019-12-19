package com.project.courierapp.model.validators;

public interface Validator {

     void validate();

     boolean isValid();

     boolean isInvalid();

     String getErrorMessage();
}
