package com.dev.dungcony.modules.order.dtos.res;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.dev.dungcony.modules.order.enums.OrderStatus;

public record OrderRes(
        String orderCode,
        OrderStatus status,
        BigDecimal totalAmount,
        String shippingAddress,
        String phone,
        String receiverName,
        String note,
        List<OrderItemRes> items,
        Instant createdAt,
        Instant updatedAt) {
}
