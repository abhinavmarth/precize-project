package com.precize.orders.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.precize.orders.model.*;
import com.precize.orders.observer.OrderObserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

//hatchling: Processing the Events
public class EventProcessor {

    private final Map<String, Order> orders = new HashMap<>();
    private final List<OrderObserver> observers = new ArrayList<>();

    private final ObjectMapper mapper;


    public EventProcessor() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public void registerObserver(OrderObserver observer){
        observers.add(observer);
    }

    public void processEvents(InputStream inputStream) throws IOException {
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                JsonNode node = mapper.readTree(line);
                String eventType = node.get("eventType").asText();

                Event event = switch (eventType) {
                    case "OrderCreated" -> parseOrderCreatedEvent(node);
                    case "PaymentReceived" -> parsePaymentReceivedEvent(node);
                    case "ShippingScheduled" -> parseShippingScheduledEvent(node);
                    case "OrderCancelled" -> parseOrderCancelledEvent(node);
                    default -> {
                        System.err.println("Warning: Unknown eventType '" + eventType + "'");
                        yield null;
                    }
                };

                if (event != null) {
                    handleEvent(event);
                }
            }
        }
    }


    private Event parseOrderCancelledEvent(JsonNode node) {
        String eventId = node.get("eventId").asText();
        Instant timestamp = Instant.parse(node.get("timestamp").asText());
        String orderId = node.get("orderId").asText();
        String reason = node.get("reason").asText();
        return new OrderCancelledEvent(eventId,timestamp,orderId,reason);
    }

    private Event parseShippingScheduledEvent(JsonNode node) {
        String eventId = node.get("eventId").asText();
        Instant timestamp = Instant.parse(node.get("timestamp").asText());
        String orderId = node.get("orderId").asText();
        LocalDate shippingate = LocalDate.parse(node.get("shippingDate").asText());
        return new ShippingScheduledEvent(eventId,timestamp,orderId,shippingate);
    }

    private Event parsePaymentReceivedEvent(JsonNode node) {
        String eventId = node.get("eventId").asText();
        Instant timestamp = Instant.parse(node.get("timestamp").asText());
        String orderId = node.get("orderId").asText();
        double amountPaid =  node.get("amountPaid").asDouble();
        return new PaymentReceivedEvent(eventId,timestamp,orderId,amountPaid);
    }

    private Event parseOrderCreatedEvent(JsonNode node) throws IOException {
        String eventId = node.get("eventId").asText();
        Instant timestamp = Instant.parse(node.get("timestamp").asText());
        String orderId = node.get("orderId").asText();
        String customerId = node.get("customerId").asText();

        List<Item> items = new ArrayList<>();
        for(JsonNode itemNode : node.withArray("items")){
            items.add(new Item(itemNode.get("itemId").asText(),itemNode.get("qty").asInt()));
        }
        double totalAmount=node.get("totalAmount").asDouble();
        return new OrderCreatedEvent(eventId,timestamp,orderId,customerId,items,totalAmount);
    }

    private void handleEvent(Event event){
        switch (event.getEventType()){
            case "OrderCreated" -> handleOrderCreated((OrderCreatedEvent) event);
            case "PaymentReceived" -> handlePaymentReceived((PaymentReceivedEvent) event);
            case "ShippingScheduled" -> handleShippingScheduled((ShippingScheduledEvent) event);
            case "OrderCancelled" -> handleOrderCancelled((OrderCancelledEvent) event);
            default -> System.err.println("Unhandled event type: "+event.getEventType());
        }
    }

    private void handleOrderCreated(OrderCreatedEvent event){
        if(orders.containsKey(event.getOrderId())){
            System.err.println("Order already exists; "+event.getOrderId());
            return;
        }
        Order order = new Order(event.getOrderId(),event.getCustomerId(),event.getItems(),event.getTotalAmount());
        orders.put(order.getOrderId(),order);
        order.addEvent(event);
        notifyObservers(order.getOrderId(),order.getStatus());
    }

    private void handlePaymentReceived(PaymentReceivedEvent event){
        Order order = orders.get(event.getOrderId());
        if(order == null ){
            System.err.println("Order not found for payment: " + event.getOrderId());
            return;
        }
        if (event.getAmountPaid() <= 0) {
            System.err.println("Invalid payment amount");
            return;
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            System.err.println("Order is cancelled. Ignoring event: " + event.getEventType());
            return;
        }

        order.addPaidAmount(event.getAmountPaid());
        OrderStatus newStatus = (order.getPaidAmount() >= order.getTotalAmount()) ? OrderStatus.PAID : OrderStatus.PARTIALLY_PAID;
        order.setStatus(newStatus);
        order.addEvent(event);
        notifyObservers(order.getOrderId(), order.getStatus());
    }

    private void handleOrderCancelled(OrderCancelledEvent event){
        Order order = orders.get(event.getOrderId());
        if(order == null){
            System.err.println("Order not found for cancellation: "+event.getOrderId());
            return;
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.addEvent(event);
        notifyObservers(order.getOrderId(), order.getStatus());
    }

    private void handleShippingScheduled(ShippingScheduledEvent event) {
        Order order = orders.get(event.getOrderId());
        if (order == null) {
            System.err.println("Order not found for shipping: " + event.getOrderId());
            return;
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            System.err.println("Order is cancelled. Ignoring event: " + event.getEventType());
            return;
        }

        order.setStatus(OrderStatus.SHIPPED);
        order.addEvent(event);
        notifyObservers(order.getOrderId(), order.getStatus());
    }


    private void notifyObservers(String orderId, OrderStatus newStatus) {
        for (OrderObserver observer : observers) {
            observer.onStatusChanged(orderId, newStatus);
        }
    }
}
