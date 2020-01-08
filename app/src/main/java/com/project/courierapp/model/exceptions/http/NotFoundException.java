package com.project.courierapp.model.exceptions.http;

public class NotFoundException extends BaseHttpException {

    private static final String ERROR_MESSAGE = "HTTP NOT FOUND: 404";
    public NotFoundException(){
        message = ERROR_MESSAGE;
    }
}
