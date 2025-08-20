package com.precize.orders.model;

import java.time.Instant;
import java.time.LocalDate;

public class ShippingScheduledEvent extends Event {
    private final String orderId;
    private final LocalDate shippingDate;

    public ShippingScheduledEvent(String eventId, Instant timeStamp, String orderId, LocalDate shippingDate) {
        super(eventId, timeStamp, "ShippingScheduled");
        this.orderId = orderId;
        this.shippingDate = shippingDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    @Override
    public String toString() {
        return "ShippingScheduledEvent{" +
                "orderId='" + orderId + '\'' +
                ", shippingDate=" + shippingDate +
                "} " + super.toString();
    }
}
