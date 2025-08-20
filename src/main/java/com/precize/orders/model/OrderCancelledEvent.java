package com.precize.orders.model;

import java.time.Instant;

public class OrderCancelledEvent extends Event {
    private final String orderId;
    private final String reason;

    public OrderCancelledEvent(String eventId, Instant time, String orderId, String reason) {
        super(eventId, time, "OrderCancelled");
        this.orderId = orderId;
        this.reason = reason;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "OrderCancelledEvent{" +
                "orderId='" + orderId + '\'' +
                ", reason='" + reason + '\'' +
                "} " + super.toString();
    }
}
