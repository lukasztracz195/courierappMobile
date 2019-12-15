package com.project.courierapp.model.validators;


import com.project.courierapp.model.dtos.request.CredentialsRequest;
import com.project.courierapp.model.validators.components.EmptyFieldsValidatorChain;

import java.util.Arrays;
import java.util.List;

public class CredentialsValidator extends BaseValidator implements Validator {

    private List<String> fields;

    private CredentialsValidator(CredentialsRequest credentialsRequest){
        fields = Arrays.asList(credentialsRequest.getUsername(),
                credentialsRequest.getPassword());
    }

    public static CredentialsValidator of(CredentialsRequest credentialsRequest){
        return new CredentialsValidator(credentialsRequest);
    }
    @Override
    public void validate() {
        validatorBuilder = ValidatorBuilder.builder()
                .add(new EmptyFieldsValidatorChain(fields))
                .validate();
    }
}
