package com.project.courierapp.model.interceptors;

import android.widget.TextView;

import com.project.courierapp.model.exceptions.http.BadRequestException;

public class CreateWorkerInterceptor extends BaseInterceptor {
    protected CreateWorkerInterceptor(TextView errorTextView, Throwable throwable) {
        super(errorTextView, InterceptorBuilder.builder()
                .add(new BadRequestException(), throwable.getMessage())
        );
    }

    public static CreateWorkerInterceptor of(TextView errorTextView, Throwable throwable){
        return new CreateWorkerInterceptor(errorTextView,throwable);
    }
}
