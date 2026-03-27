package com.dev.dungcony.modules.order.dtos.res;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.dev.dungcony.modules.order.dtos.OrderItemDto;
import com.dev.dungcony.modules.order.enums.OrderStatus;
import com.dev.dungcony.modules.users.dtos.RecieverDto;

public record OrderRes(
        String orderCode,
        OrderStatus status,
        BigDecimal totalAmount,
        RecieverDto reciever,
        String note,
        List<OrderItemDto> items,
        Instant createdAt,
        Instant updatedAt) {
}
