package com.project.courierapp.model.interceptors;

import com.project.courierapp.model.exceptions.http.BaseHttpException;

import java.util.HashMap;
import java.util.Map;

public class InterceptorBuilder {

    private final String NOT_FOUND_EXCEPTION = "NOT FOUND EXCEPTION";
    private Map<Class<? extends Throwable>,String> mapException;

    protected InterceptorBuilder(){
        mapException = new HashMap<>();
    }

    public static InterceptorBuilder builder(){
        return  new InterceptorBuilder();
    }

    public InterceptorBuilder add(BaseHttpException exception, String errorMessage){
        mapException.put(exception.getClass(),errorMessage);
        return this;
    }

    protected String getError(Throwable exception){
        if(mapException.containsKey(exception.getClass())){
            return mapException.get(exception.getClass());
        }
        return  NOT_FOUND_EXCEPTION;
    }
}
