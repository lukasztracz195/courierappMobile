package com.project.courierapp.model.exceptions.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BaseHttpException extends Exception {

    private String message;
}
