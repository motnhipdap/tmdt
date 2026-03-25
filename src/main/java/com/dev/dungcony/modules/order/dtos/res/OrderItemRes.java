package com.dev.dungcony.modules.order.dtos.res;

import java.math.BigDecimal;

public record OrderItemRes(
        String productCode,
        String productName,
        String productImg,
        String sizeName,
        Integer quantity,
        BigDecimal price,
        BigDecimal subtotal) {
}
