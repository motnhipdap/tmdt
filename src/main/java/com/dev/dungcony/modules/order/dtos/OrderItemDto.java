package com.dev.dungcony.modules.order.dtos;

import com.dev.dungcony.modules.product.enums.ProductSize;

import java.math.BigDecimal;

public record OrderItemDto(
        String productCode,
        ProductSize size,
        int quantity,
        BigDecimal price
) {
}
