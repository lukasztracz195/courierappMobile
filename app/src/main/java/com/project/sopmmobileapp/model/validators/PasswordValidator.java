package com.project.sopmmobileapp.model.validators;


import com.project.sopmmobileapp.R;
import com.project.sopmmobileapp.model.dtos.request.CredentialsRequest;
import com.project.sopmmobileapp.model.dtos.request.RegisterCredentialsRequest;

import java.util.HashSet;
import java.util.Set;

public class PasswordValidator {

    private static int errorMessageCode;

    public static boolean valid(RegisterCredentialsRequest registerCredentialsRequest) {
        String password = registerCredentialsRequest.getPassword();
        String repeatPassword = registerCredentialsRequest.getRepeatPassword();
        String username = registerCredentialsRequest.getUsername();
        Set<Boolean> validateSet = new HashSet<>();
        validateSet.add(checkIsEmptyFields(username, password, repeatPassword));
        validateSet.add(checkIsNotTheSamePasswords(password, repeatPassword));
        return !validateSet.contains(true);
    }

    public static boolean valid(CredentialsRequest credentialsRequest) {
        String password = credentialsRequest.getPassword();
        String username = credentialsRequest.getUsername();
        Set<Boolean> validateSet = new HashSet<>();
        validateSet.add(checkIsEmptyFields(username, password));
        return !validateSet.contains(true);
    }

    public static CredentialsRequest toCredential(RegisterCredentialsRequest registerCredentialsRequest) {
        CredentialsRequest credential = new CredentialsRequest();
        credential.setUsername(registerCredentialsRequest.getUsername());
        credential.setPassword(registerCredentialsRequest.getPassword());
        return credential;
    }


    private static boolean checkIsEmptyFields(String username, String password, String repeatPassword) {
        if (password.isEmpty() || repeatPassword.isEmpty() || username.isEmpty()) {
            errorMessageCode = R.string.empty_fields;
            return true;
        }
        return false;
    }

    private static boolean checkIsEmptyFields(String username, String password) {
        if (password.isEmpty() || username.isEmpty()) {
            errorMessageCode = R.string.empty_fields;
            return true;
        }
        return false;
    }

    private static boolean checkIsNotTheSamePasswords(String password, String repeatPassword) {
        if (!password.equals(repeatPassword)){
            errorMessageCode = R.string.uncorrect_password;
            return true;
        }
        return false;
    }

    public static int getErrorMessageCode() {
        return errorMessageCode;
    }
}
