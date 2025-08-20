package com.precize.orders.observer;


import com.precize.orders.model.Order;
import com.precize.orders.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertObserver implements OrderObserver{
    @Override
    public void onStatusChanged(String orderId, OrderStatus newStatus) {
        Logger logger = LoggerFactory.getLogger(AlertObserver.class);
        if(OrderStatus.CANCELLED.equals(newStatus) || OrderStatus.SHIPPED.equals(newStatus)){
            logger.warn("ALERT: Sending alert for Order {} Status changed to {}",orderId,newStatus);
        }
    }
}
