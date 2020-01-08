package com.project.courierapp.model.validators;


import com.project.courierapp.model.dtos.request.CredentialsRequest;
import com.project.courierapp.model.validators.components.EmptyFieldsValidatorChain;
import com.project.courierapp.model.validators.components.NumberCharsValidatorChain;
import com.project.courierapp.model.validators.components.WhiteCharsValidatorChain;

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
                .add(EmptyFieldsValidatorChain.of(fields))
                .add(WhiteCharsValidatorChain.of(fields.get(0)))
                .add(WhiteCharsValidatorChain.of(fields.get(1)))
                .add(NumberCharsValidatorChain.of(fields.get(0),4,20))
                .add(NumberCharsValidatorChain.of(fields.get(1),4,20))
                .validate();
    }
}
