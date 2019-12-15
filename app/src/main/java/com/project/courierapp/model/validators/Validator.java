package com.project.courierapp.model.validators;

import java.util.List;

public interface Validator {

     void validate();

     boolean isValid();

     List<String> getErrorMessages();
}
