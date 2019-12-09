package com.project.courierapp.model.validators;


import com.project.courierapp.model.dtos.request.CredentialsRequest;

import java.util.Arrays;

public class PasswordValidator extends BaseValidator{

    public static boolean valid(CredentialsRequest credentialsRequest) {
        String password = credentialsRequest.getPassword();
        String username = credentialsRequest.getUsername();
        validatorBuilder.add(checkIsEmptyFields(Arrays.asList(username, password)));
        return !validatorBuilder.valid();
    }


}
