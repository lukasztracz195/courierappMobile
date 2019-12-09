package com.project.courierapp.model.validators;

import com.project.courierapp.R;

import java.util.List;

public class BaseValidator {

   protected static int errorMessageCode;

    protected static ValidatorBuilder validatorBuilder = new ValidatorBuilder();

    protected static boolean checkIsEmptyFields(List<String> fields) {

        for(String field : fields){
            if(field.isEmpty()){
                errorMessageCode = R.string.empty_fields_error;
                return true;
            }
        }
        return  false;
    }

    public static int getErrorMessageCode() {
        return errorMessageCode;
    }
}
