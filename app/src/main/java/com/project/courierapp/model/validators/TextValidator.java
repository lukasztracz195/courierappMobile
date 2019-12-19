package com.project.courierapp.model.validators;

import com.project.courierapp.model.validators.components.EmptyFieldsValidatorChain;
import com.project.courierapp.model.validators.components.ValidatorChain;

import java.util.Collections;
import java.util.List;

import lombok.Setter;

@Setter
public class TextValidator extends BaseValidator implements Validator{

    private String textToValidation;

    private TextValidator(List<ValidatorChain> validatorChains){
       this.validatorBuilder = ValidatorBuilder.builder()
               .add(EmptyFieldsValidatorChain.of(Collections.singletonList(textToValidation)));
        for (ValidatorChain validatorChain : validatorChains) {
            validatorBuilder.add(validatorChain);
        }
    }

    public static TextValidator of(List<ValidatorChain> validatorChains){
        return new TextValidator((validatorChains));
    }


    public void setTextToValidation(String textToValidation){
        this.textToValidation = textToValidation;
        List<ValidatorChain> listValidatorsChains = validatorBuilder.getValidatorChains();
        for(ValidatorChain validatorChain : listValidatorsChains){
            validatorChain.setTextToValidation(textToValidation);
        }
    }

    @Override
    public void validate() {
        super.validate();
        validatorBuilder.validate();
    }


}
