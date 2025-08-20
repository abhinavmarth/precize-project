package com.precize.orders.observer;

import com.precize.orders.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerObserver implements OrderObserver{

    Logger logger = LoggerFactory.getLogger(LoggerObserver.class);
    @Override
    public void onStatusChanged(String orderId, OrderStatus newstatus) {
        logger.info("LOG: Order {}  changed to {}",orderId, newstatus);
    }
}
