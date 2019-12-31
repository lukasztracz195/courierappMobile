package com.project.courierapp.model.validators.components;

import android.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class EmptyFieldsValidatorChain implements ValidatorChain {

    private List<String> fieldsToValid;
    private final static String ERROR_MESSAGE = "Field cannot be empty";

    private EmptyFieldsValidatorChain(List<String> fieldsToValid){
    this.fieldsToValid = fieldsToValid;

    }

    public static EmptyFieldsValidatorChain of(List<String> fieldsToValid){
        return new EmptyFieldsValidatorChain(fieldsToValid);
    }

    public Pair<Boolean, String> validate(){
            return checkEmptyFields();
    }

    @Override
    public void setTextToValidation(String textToValidation) {
        fieldsToValid = Collections.singletonList(textToValidation);
    }

    private Pair<Boolean,String> checkEmptyFields(){
        for (String field : fieldsToValid) {
            Optional<String> stringOptional = Optional.ofNullable(field);
            if (!stringOptional.isPresent() || stringOptional.get().isEmpty()) {
                return  new Pair<>(false, ERROR_MESSAGE);
            }
        }
        return new Pair<>(true, VALIDATION_PASSED);
    }

}
