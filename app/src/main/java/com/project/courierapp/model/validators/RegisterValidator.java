package com.project.courierapp.model.validators;

import com.project.courierapp.model.dtos.request.RegisterCredentialsRequest;
import com.project.courierapp.model.validators.components.EmptyFieldsValidatorChain;
import com.project.courierapp.model.validators.components.RegexValidatorChain;

import java.util.Arrays;
import java.util.List;

public class RegisterValidator extends BaseValidator implements Validator {

    private static final String EMAIL_REGEX = "\"^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$\"";
    private static final String EMAIL_ERROR_VALIDATION = " Email is wrong";
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
                .add(RegexValidatorChain.of(registerCredentialsRequest.getEmail(),
                        EMAIL_REGEX, EMAIL_ERROR_VALIDATION))
                .validate();
    }
}
