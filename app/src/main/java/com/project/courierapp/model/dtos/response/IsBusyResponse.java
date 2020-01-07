package com.project.courierapp.model.dtos.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class IsBusyResponse {

    @SerializedName("busy")
    private boolean isBusy;
}
