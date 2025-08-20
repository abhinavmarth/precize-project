package com.precize.orders.model;

import java.time.Instant;
import java.util.List;

public class OrderCreatedEvent extends Event {
    private final String orderId;
    private final String customerId;
    private final List<Item> items;
    private final double totalAmount;

    public OrderCreatedEvent(String eventId, Instant time, String orderId, String customerId, List<Item> items, double totalAmount) {
        super(eventId, time, "OrderCreated");
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<Item> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", items=" + items +
                ", totalAmount=" + totalAmount +
                "} " + super.toString();
    }
}
