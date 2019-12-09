package com.project.courierapp.model.validators;

import com.project.courierapp.model.dtos.request.RegisterCredentialsRequest;

import java.util.Arrays;

public class RegisterValidator extends BaseValidator{

    private static final String EMAIL_REGEX = "\"^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$\"";

    public static boolean valid(RegisterCredentialsRequest registerCredentialsRequest){
        String username = registerCredentialsRequest.getLogin();
        String email = registerCredentialsRequest.getEmail();
        return validatorBuilder.add(checkIsEmptyFields(Arrays.asList(username, email)))
                .add(validEmail(email))
                .valid();
    }


    private static boolean validEmail(String email){
        return email.matches(EMAIL_REGEX);
    }
}
