package com.project.courierapp.model.exceptions.http;

public class ServerErrorException extends BaseHttpException {

    private static final String ERROR_MESSAGE = "HTTP SERVER ERROR: 500";

    public ServerErrorException() {
        message = ERROR_MESSAGE;
    }
}
