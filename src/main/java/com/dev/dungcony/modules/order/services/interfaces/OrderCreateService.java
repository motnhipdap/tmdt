package com.dev.dungcony.modules.order.services.interfaces;

import com.dev.dungcony.modules.order.dtos.req.CreateOrderReq;

import java.util.UUID;

public interface OrderCreateService {
    void createOrder(UUID userId, CreateOrderReq req);
}
