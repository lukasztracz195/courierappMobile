package com.project.courierapp.view.holders;

import com.project.courierapp.model.dtos.response.Response;

import java.util.List;

public interface Holder {

    void initTextViews(List<Integer> ids);

    void setFields(Response object);
}
