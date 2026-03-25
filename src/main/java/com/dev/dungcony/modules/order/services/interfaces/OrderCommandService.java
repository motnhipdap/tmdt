package com.dev.dungcony.modules.order.services.interfaces;

import java.util.UUID;

import com.dev.dungcony.modules.order.dtos.req.CreateOrderReq;
import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.enums.OrderStatus;

public interface OrderCommandService {

    OrderRes createOrderFromCart(UUID userId, CreateOrderReq req);

    void cancelOrder(UUID userId, String orderCode);

    void updateOrderStatus(String orderCode, OrderStatus status);
}
