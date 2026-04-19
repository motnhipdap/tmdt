package com.dev.dungcony.modules.cart.dtos;

import com.dev.dungcony.modules.product.enums.ProductSize;

import java.math.BigDecimal;

public record CartItemDto(
        int productId,
        String productCode,
        ProductSize productSize,
        String categoryCode,
        String providerCode,
        BigDecimal originalPrice,
        BigDecimal finalPrice
) {
}
