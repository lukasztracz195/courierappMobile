package com.project.courierapp.view.adapters;

import com.project.courierapp.model.dtos.response.Response;

import java.util.List;

public interface Adapter {

    void downloadData();

    void updateData(Response response);

    void updateData(List<? extends Response> responses);
}
