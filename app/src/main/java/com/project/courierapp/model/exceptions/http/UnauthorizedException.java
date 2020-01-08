package com.project.courierapp.model.exceptions.http;

public class UnauthorizedException extends BaseHttpException {

    private static final String ERROR_MESSAGE = "HTTP ACCESS IS FORBIDDEN: 401";
    public UnauthorizedException(){
        message = ERROR_MESSAGE;
    }
}
