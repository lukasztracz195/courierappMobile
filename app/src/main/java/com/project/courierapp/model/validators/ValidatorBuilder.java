package com.project.courierapp.model.validators;

import java.util.HashSet;
import java.util.Set;

public class ValidatorBuilder {

    private  Set<Boolean> validateSet = new HashSet<>();


    public  ValidatorBuilder add(boolean validator){
        validateSet.add(validator);
        return this;
    }

    public  boolean valid(){
        return !validateSet.contains(true);
    }
}
