package com.dev.dungcony.modules.order.services.interfaces;

import com.dev.dungcony.modules.order.enums.OrderStatus;

import java.util.UUID;

public interface OrderUpdateService {
    void cancelOrder(UUID userId, String orderCode);

    void paidOrder(UUID userId, String orderCode);

    void completedOrder(UUID userId, String orderCode);

    void confirmOrder(UUID userId, String orderCode);

    void updateOrderStatus(String orderCode, OrderStatus nextStatus);
}
