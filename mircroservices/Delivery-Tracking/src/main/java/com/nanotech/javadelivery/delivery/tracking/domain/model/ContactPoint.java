package com.nanotech.javadelivery.delivery.tracking.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
public class ContactPoint {

    private String zipCod;
    private  String  street;
    private  String number;
    private  String complement;
    private String name;
    private  String phone;
}
