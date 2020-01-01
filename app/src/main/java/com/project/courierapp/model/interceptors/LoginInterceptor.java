package com.project.courierapp.model.interceptors;

import android.widget.TextView;

import com.project.courierapp.model.exceptions.http.BadRequestException;
import com.project.courierapp.model.exceptions.http.NotFoundException;
import com.project.courierapp.model.exceptions.http.ServerErrorException;
import com.project.courierapp.model.exceptions.http.UnauthorizedException;

public class LoginInterceptor extends BaseInterceptor {
    protected LoginInterceptor(TextView errorTextView) {
        super(errorTextView, InterceptorBuilder.builder()
        .add(new UnauthorizedException(), "Username or password is wrong")
        .add(new BadRequestException(), "Http request is wrong")
        .add(new NotFoundException(),"This user not existed")
        .add(new ServerErrorException(), "Server error"));
    }

    public static LoginInterceptor of(TextView errorTextView){
        return new LoginInterceptor(errorTextView);
    }
}
