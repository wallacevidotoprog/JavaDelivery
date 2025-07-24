package com.nanotech.javadelivery.delivery.tracking.domain.model;

import com.nanotech.javadelivery.delivery.tracking.domain.excepition.DomainException;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {

    @EqualsAndHashCode.Include
    private UUID id;
    private UUID courierId;


    private DeliveryStatus status;

    private OffsetDateTime placeAt;
    private OffsetDateTime assingedAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fullfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    private ContactPoint sender;
    private ContactPoint recipient;

    private List<Item> items = new ArrayList<>();

    public static Delivery draft() {
        Delivery delivery = new Delivery();
        delivery.setId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.DRAFT);
        delivery.setTotalItems(0);
        delivery.setTotalCost(BigDecimal.ZERO);
        delivery.setCourierPayout(BigDecimal.ZERO);
        delivery.setDistanceFee(BigDecimal.ZERO);
        return delivery;
    }

    private void calculateTotalItems() {
        int totalItems = items.stream().mapToInt(Item::getQuantity).sum();

        setTotalItems(totalItems);
    }

    public void changeItemQuantity(UUID itemId, int quantity) {
        Item item = getItems().stream().filter(i -> i.getId().equals(itemId))
                .findFirst().orElseThrow();

        item.setQuantity(quantity);
        calculateTotalItems();
    }

    public UUID addItem(String name, Integer quantity) {
        Item item = Item.brandNew(name, quantity);
        items.add(item);
        calculateTotalItems();
        return item.getId();
    }

    public void removeItem(UUID itemId) {
        items.removeIf(item -> item.getId().equals(itemId));
        calculateTotalItems();
    }

    public void removeAllItems() {
        items.clear();
        calculateTotalItems();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }


    public void place() {
        verifyIfCanBePlaced();
        this.setStatus(DeliveryStatus.WAITING_FOR_COURIER);
        this.setPlaceAt(OffsetDateTime.now());
    }

    public void pickUp(UUID courierId) {
        this.setCourierId(courierId);
        this.setStatus(DeliveryStatus.IN_TRANSIT);
        this.setAssingedAt(OffsetDateTime.now());
    }

    public void markAsDelivered() {
        this.setStatus(DeliveryStatus.DELIVERY);
        this.setFullfilledAt(OffsetDateTime.now());
    }

    private void verifyIfCanBePlaced() {
        if (!isFilled()) {
            throw new DomainException();
        }

        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new DomainException();
        }
    }

    private boolean isFilled() {
        return this.getSender() != null && this.getRecipient() != null && this.getTotalCost() != null;
    }

    public void editPreparationDetails(PreparationDetails details) {
        verifyIfCanBeEdited();

        setSender(details.getSender());
        setRecipient(details.getRecipient());
        setDistanceFee(details.getDistanceFee());
        setCourierPayout(details.getCourierPayout());

        setExpectedDeliveryAt(OffsetDateTime.now().plus(details.getExpectedDeliveryTime()));
        setTotalCost(this.getDistanceFee().add(this.getCourierPayout()));
    }

    private void verifyIfCanBeEdited() {
        if (!getStatus().equals(DeliveryStatus.DRAFT)) {
            throw new DomainException();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PreparationDetails {
        private ContactPoint sender;
        private ContactPoint recipient;

        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private Duration expectedDeliveryTime;
    }
}
