package com.precize.orders.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String orderId;
    private final String customerId;
    private final List<Item> items;
    private final double totalAmount;

    private double paidAmount = 0.0;
    private OrderStatus status = OrderStatus.PENDING;
    private final List<Event> eventHistory = new ArrayList<>();

    public Order(String orderId, String customerId, List<Item> items, double totalAmount) {
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

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public void addPaidAmount(double amt) {
        this.paidAmount += amt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<Event> getEventHistory() {
        return eventHistory;
    }

    public void addEvent(Event event) {
        eventHistory.add(event);
    }
}
