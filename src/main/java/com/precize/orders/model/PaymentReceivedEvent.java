package com.precize.orders.model;

import java.time.Instant;

public class PaymentReceivedEvent extends Event {
    private final String orderId;
    private final double amountPaid;

    public PaymentReceivedEvent(String eventId, Instant time, String orderId, double amountPaid) {
        super(eventId, time, "PaymentReceived");
        this.orderId = orderId;
        this.amountPaid = amountPaid;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    @Override
    public String toString() {
        return "PaymentReceivedEvent{" +
                "orderId='" + orderId + '\'' +
                ", amountPaid=" + amountPaid +
                "} " + super.toString();
    }
}
