package com.project.courierapp.model.validators;

import com.project.courierapp.model.dtos.transfer.DeliveryPointDto;
import com.project.courierapp.model.validators.components.EmptyFieldsValidatorChain;
import com.project.courierapp.model.validators.components.RegexValidatorChain;

import java.util.Arrays;
import java.util.List;

public class DeliveryPointDtoValidator extends BaseValidator implements Validator {

    private static final String POSTAL_CODE_REGEX = "[0-9]{2}\\-[0-9]{3}";
    private static final String POSTAL_CODE_ERROR_VALIDATION = "Postal code is wrong";
    private List<String> fields;
    private DeliveryPointDto deliveryPointDto;

    private DeliveryPointDtoValidator(DeliveryPointDto deliveryPointDto) {
        this.deliveryPointDto = deliveryPointDto;
        fields = Arrays.asList(
                deliveryPointDto.getAddress(),
                deliveryPointDto.getPostalCode(),
                deliveryPointDto.getCity(),
                deliveryPointDto.getCountry());
    }

    public static DeliveryPointDtoValidator of( DeliveryPointDto deliveryPointDto){
        return  new DeliveryPointDtoValidator((deliveryPointDto));
    }

    @Override
    public void validate() {
       validatorBuilder = ValidatorBuilder.builder()
                .add(new EmptyFieldsValidatorChain(fields))
                .add(new RegexValidatorChain(deliveryPointDto.getPostalCode(),
                        POSTAL_CODE_REGEX, POSTAL_CODE_ERROR_VALIDATION))
                .validate();
    }
}


