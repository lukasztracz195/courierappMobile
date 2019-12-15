package com.project.courierapp.model.dtos.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AddDeliveryPointsRequest {

    private List<AddDeliveryPointRequest> deliveryPointRequests;


}
