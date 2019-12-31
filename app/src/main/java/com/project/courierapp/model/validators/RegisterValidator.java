package com.project.courierapp.model.validators;

import com.project.courierapp.model.dtos.request.RegisterCredentialsRequest;
import com.project.courierapp.model.validators.components.EmailValidatorChain;
import com.project.courierapp.model.validators.components.EmptyFieldsValidatorChain;

import java.util.Arrays;
import java.util.List;

public class RegisterValidator extends BaseValidator implements Validator {

    private RegisterCredentialsRequest registerCredentialsRequest;
    private List<String> fields;

    private RegisterValidator(RegisterCredentialsRequest registerCredentialsRequest) {
        this.registerCredentialsRequest = registerCredentialsRequest;
        fields = Arrays.asList(registerCredentialsRequest.getLogin(),
                registerCredentialsRequest.getEmail());
    }

    public static RegisterValidator of(RegisterCredentialsRequest registerCredentialsRequest ){
        return new RegisterValidator((registerCredentialsRequest));
    }

    @Override
    public void validate() {
        validatorBuilder = ValidatorBuilder.builder()
                .add(EmptyFieldsValidatorChain.of(fields))
                .add(EmailValidatorChain.of(registerCredentialsRequest.getEmail()))
                .validate();
    }
}
