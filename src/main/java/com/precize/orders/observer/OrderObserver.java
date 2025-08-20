package com.precize.orders.observer;

import com.precize.orders.model.Order;
import com.precize.orders.model.OrderStatus;

public interface OrderObserver {

    void onStatusChanged(String orderId, OrderStatus newstatus);
}
