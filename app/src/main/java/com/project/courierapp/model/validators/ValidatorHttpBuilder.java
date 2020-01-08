package com.project.courierapp.model.validators;

import android.util.Log;

import com.project.courierapp.model.exceptions.http.BaseHttpException;
import com.project.courierapp.model.exceptions.http.NotFoundException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

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

    public Exception validate(String clientTag, Response<? extends Object> response) throws IOException {
        if (validatorsMap.containsKey(response.code())) {
            Log.i(clientTag, "HTTP_CODE: " + response.code());
            Log.i(clientTag, response.message());
            if (response.errorBody() != null) {
                Log.e(clientTag, response.errorBody().toString());
            }
            BaseHttpException baseHttpException = validatorsMap.get(response.code());
            if (response.errorBody() != null) {
                Objects.requireNonNull(baseHttpException).setMessage(response.errorBody().string());
            } else if (!response.message().isEmpty()) {
                Objects.requireNonNull(baseHttpException).setMessage(response.message());
            } else if (!baseHttpException.getMessage().isEmpty()) {
                Objects.requireNonNull(baseHttpException).setMessage(response.message());
            } else {
                Objects.requireNonNull(baseHttpException).setMessage(baseHttpException.getClass().getName());
            }
            return baseHttpException;
        }
        return new NotFoundException();
    }
}
