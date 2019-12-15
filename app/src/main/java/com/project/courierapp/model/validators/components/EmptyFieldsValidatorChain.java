package com.project.courierapp.model.validators.components;

import android.util.Pair;

import java.util.List;

public class EmptyFieldsValidatorChain implements ValidatorChain {

    private List<String> fieldsToValid;
    private final static String ERROR_MESSAGE = "fields must not is blank";

    public EmptyFieldsValidatorChain(List<String> fieldsToValid){
    this.fieldsToValid = fieldsToValid;

    }

    public Pair<Boolean, String> validate(){
            return checkEmptyFields();
    }

    private Pair<Boolean,String> checkEmptyFields(){
        for (String field : fieldsToValid) {
            if (field.isEmpty()) {
                return  new Pair<>(false, ERROR_MESSAGE);
            }
        }
        return new Pair<>(true, VALIDATION_PASSED);
    }

}
