package com.dev.dungcony.modules.order.dtos.req;

import com.dev.dungcony.modules.order.dtos.OrderItemDto;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderReq(
        int recieverid,
        List<OrderItemDto> items,
        BigDecimal totalPrice,
        String voucherCode,
        BigDecimal voucherDiscount,
        BigDecimal finalPrice,
        @Size(max = 500) String note) {
}
