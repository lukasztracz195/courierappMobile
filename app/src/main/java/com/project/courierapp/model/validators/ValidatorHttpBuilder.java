package com.project.courierapp.model.validators;

import android.util.Log;

import com.project.courierapp.model.exceptions.http.BaseHttpException;
import com.project.courierapp.model.exceptions.http.NotFoundException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import retrofit2.Response;

public class ValidatorHttpBuilder {

    private Map<Integer, BaseHttpException> validatorsMap;

    private ValidatorHttpBuilder() {
        this.validatorsMap = new LinkedHashMap<>();
    }

    public static ValidatorHttpBuilder builder() {
        return new ValidatorHttpBuilder();
    }

    public ValidatorHttpBuilder addValidator(Integer httpCode, BaseHttpException exceptionToThrow) {
        validatorsMap.put(httpCode, exceptionToThrow);
        return this;
    }

    public Exception validate(String clientTag, Response<? extends Object> response) {
        if (validatorsMap.containsKey(response.code())) {
            Log.i(clientTag, "HTTP_CODE: " + response.code());
            Log.i(clientTag, response.message());
            if (response.errorBody() != null) {
                Log.e(clientTag, response.errorBody().toString());
            }
            BaseHttpException baseHttpException = validatorsMap.getOrDefault(response.code(),
                    new BaseHttpException());
            Optional.ofNullable(response.errorBody()).ifPresent(responseBody ->
            {
                try {
                    baseHttpException.setMessage(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return baseHttpException;
        }
        return new NotFoundException();
    }
}
