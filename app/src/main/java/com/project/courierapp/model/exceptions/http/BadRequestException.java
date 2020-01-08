package com.project.courierapp.model.exceptions.http;

public class BadRequestException extends BaseHttpException {

    private static final String ERROR_MESSAGE = "HTTP BAD REQUEST: 401";
    public BadRequestException(){
        message = ERROR_MESSAGE;
    }
}
