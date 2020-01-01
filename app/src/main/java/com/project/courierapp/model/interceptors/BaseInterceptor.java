package com.project.courierapp.model.interceptors;

import android.widget.TextView;

public class BaseInterceptor {

    protected TextView errorTextView;
    protected InterceptorBuilder interceptorBuilder;

    public BaseInterceptor(TextView errorTextView, InterceptorBuilder interceptorBuilder){
        this.errorTextView = errorTextView;
        this.interceptorBuilder = interceptorBuilder;
    }

    public String getError(Throwable exception){
        String error =  interceptorBuilder.getError(exception);
        errorTextView.setText(error);
        return error;
    }

}
