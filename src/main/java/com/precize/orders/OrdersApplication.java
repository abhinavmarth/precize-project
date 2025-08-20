package com.precize.orders;

import com.precize.orders.observer.AlertObserver;
import com.precize.orders.observer.LoggerObserver;
import com.precize.orders.processor.EventProcessor;

import java.io.InputStream;

// hatchling: assignment entry point
public class OrdersApplication {

    public static void main(String[] args) throws Exception {
        EventProcessor processor = new EventProcessor();
        processor.registerObserver(new LoggerObserver());
        processor.registerObserver(new AlertObserver());

        // Load events.json from resources
        try (InputStream inputStream = OrdersApplication.class.getClassLoader().getResourceAsStream("events.json")) {
            if (inputStream == null) {
                throw new RuntimeException("events.json not found in resources folder");
            }
            processor.processEvents(inputStream);
        }
    }
}
