package com.precize.orders.processor;


import com.precize.orders.model.OrderStatus;
import com.precize.orders.observer.OrderObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.*;

public class EventProcessorTest {

    private EventProcessor eventProcessor;
    private OrderObserver observer;

    @BeforeEach
    void setUp(){
        System.out.println("BEFORE EACH");
        eventProcessor = new EventProcessor();
        observer = mock(OrderObserver.class);
        eventProcessor.registerObserver(observer);
    }
    @Test
    void shouldProcessOrderCreatedEvent() throws Exception{
        String orderCreated = """
    {"eventId":"e1","timestamp":"2025-08-01T10:00:00Z","eventType":"OrderCreated","orderId":"ORD123","customerId":"CUST456","items":[{"itemId":"ITEM1","qty":2},{"itemId":"ITEM2","qty":1}],"totalAmount":150.0}
    """;
        InputStream node = new ByteArrayInputStream(orderCreated.getBytes());
        eventProcessor.processEvents(node);
        verify(observer,times(1)).onStatusChanged("ORD123", OrderStatus.PENDING);
    }

    @Test
    void shouldUpdateStatusToPaidWhenFullPaymentReceived() throws Exception{
        String orderCreated = """
    {"eventId":"e1","timestamp":"2025-08-01T10:00:00Z","eventType":"OrderCreated","orderId":"ORD123","customerId":"CUST456","items":[{"itemId":"ITEM1","qty":2},{"itemId":"ITEM2","qty":1}],"totalAmount":150.0}
    """;
        String paymentReceived1 = """
    {"eventId":"e2","timestamp":"2025-08-01T10:05:00Z","eventType":"PaymentReceived","orderId":"ORD123","amountPaid":140.0}
    """;

        InputStream node1 = new ByteArrayInputStream(orderCreated.getBytes());
        InputStream node2 = new ByteArrayInputStream(paymentReceived1.getBytes());
        eventProcessor.processEvents(node1);
        eventProcessor.processEvents(node2);
        verify(observer, times(1)).onStatusChanged("ORD123", OrderStatus.PENDING);
        verify(observer, times(1)).onStatusChanged("ORD123", OrderStatus.PARTIALLY_PAID);
    }

    @Test
    void shouldNotAcceptPaymentAfterCancellation() throws Exception {
        String orderCreated = """
    {"eventId":"e1","timestamp":"2025-08-01T10:00:00Z","eventType":"OrderCreated","orderId":"ORD100","customerId":"CUST100","items":[{"itemId":"ITEM1","qty":1}],"totalAmount":100.0}
    """;
        String orderCancelled = """
    {"eventId":"e2","timestamp":"2025-08-01T10:10:00Z","eventType":"OrderCancelled","orderId":"ORD100","reason":"User requested cancellation"}
    """;
        String paymentAfterCancel = """
    {"eventId":"e3","timestamp":"2025-08-01T10:15:00Z","eventType":"PaymentReceived","orderId":"ORD100","amountPaid":100.0}
    """;

        InputStream stream1 = new ByteArrayInputStream(orderCreated.getBytes());
        InputStream stream2 = new ByteArrayInputStream(orderCancelled.getBytes());
        InputStream stream3 = new ByteArrayInputStream(paymentAfterCancel.getBytes());

        eventProcessor.processEvents(stream1);
        eventProcessor.processEvents(stream2);
        eventProcessor.processEvents(stream3);

        verify(observer, times(1)).onStatusChanged("ORD100", OrderStatus.PENDING);
        verify(observer, times(1)).onStatusChanged("ORD100", OrderStatus.CANCELLED);
        verify(observer, never()).onStatusChanged("ORD100", OrderStatus.PAID);
    }

    @Test
    void shouldNotCreateDuplicateOrders() throws Exception {
        String orderCreated = """
    {"eventId":"e1","timestamp":"2025-08-01T09:00:00Z","eventType":"OrderCreated","orderId":"ORD200","customerId":"CUST200","items":[{"itemId":"ITEM1","qty":2}],"totalAmount":200.0}
    """;

        InputStream stream1 = new ByteArrayInputStream(orderCreated.getBytes());
        InputStream stream2 = new ByteArrayInputStream(orderCreated.getBytes()); // same event repeated

        eventProcessor.processEvents(stream1);
        eventProcessor.processEvents(stream2);

        verify(observer, times(1)).onStatusChanged("ORD200", OrderStatus.PENDING);
    }

    @Test
    void shouldIgnoreInvalidNegativePayment() throws Exception {
        String orderCreated = """
    {"eventId":"e1","timestamp":"2025-08-01T08:00:00Z","eventType":"OrderCreated","orderId":"ORD300","customerId":"CUST300","items":[{"itemId":"ITEM1","qty":1}],"totalAmount":100.0}
    """;
        String invalidPayment = """
    {"eventId":"e2","timestamp":"2025-08-01T08:10:00Z","eventType":"PaymentReceived","orderId":"ORD300","amountPaid":-50.0}
    """;

        InputStream stream1 = new ByteArrayInputStream(orderCreated.getBytes());
        InputStream stream2 = new ByteArrayInputStream(invalidPayment.getBytes());

        eventProcessor.processEvents(stream1);
        eventProcessor.processEvents(stream2);

        verify(observer, times(1)).onStatusChanged("ORD300", OrderStatus.PENDING);
        verify(observer, never()).onStatusChanged("ORD300", OrderStatus.PARTIALLY_PAID);
    }


}
