package com.nanotech.javadelivery.delivery.tracking.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

class DeliveryTest {
    @Test
    public void shoudChangeStatusToPlaced() {

        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createValidPreparationDetails());

        delivery.place();


    }

    @Test
    public void shoudNotChangeStatusToPlaced() {

        Delivery delivery = Delivery.draft();

        // delivery.editPreparationDetails(createValidPreparationDetails());

        delivery.place();


    }

    private Delivery.PreparationDetails createValidPreparationDetails() {
        ContactPoint sender = ContactPoint.builder()
                                          .zipCod("asdasdas")
                                          .street("rua")
                                          .number("312")
                                          .complement("nao sei")
                                          .name("wallace")
                                          .phone("123124")
                                          .build();

        ContactPoint recipient = ContactPoint.builder()
                                             .zipCod("vvvvvv")
                                             .street("rua 2")
                                             .number("111")
                                             .complement("naho sei")
                                             .name("vidoto")
                                             .phone("3325677")
                                             .build();


        return Delivery.PreparationDetails.builder()
                                          .sender(sender)
                                          .recipient(recipient)
                                          .distanceFee(new BigDecimal("13.43"))
                                          .courierPayout(new BigDecimal("2.4"))
                                          .expectedDeliveryTime(Duration.ofHours(5))
                                          .build();
    }
}