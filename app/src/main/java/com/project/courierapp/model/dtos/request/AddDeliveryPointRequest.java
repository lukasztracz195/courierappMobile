package com.project.courierapp.model.dtos.request;

import com.google.gson.annotations.SerializedName;
import com.project.courierapp.model.builders.AddressBuilder;
import com.project.courierapp.model.dtos.transfer.DeliveryPointDto;

import org.joda.time.Duration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddDeliveryPointRequest implements Request{

    @SerializedName("address")
    String address;

    @SerializedName("expectedSpendTime")
    String expectedSpendTime;

    public static AddDeliveryPointRequest of(DeliveryPointDto deliveryPointDto){
        return AddDeliveryPointRequest.builder()
                .address(AddressBuilder.builder()
                .add(deliveryPointDto.getAddress())
                .add(deliveryPointDto.getPostalCode())
                .add(deliveryPointDto.getCity())
                .add(deliveryPointDto.getCountry())
                .build())
                .expectedSpendTime(Duration.standardMinutes(
                Long.parseLong(deliveryPointDto.getExpendedTime())).toString()).build();

    }
}
