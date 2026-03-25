package com.dev.dungcony.modules.cart.dtos;

import com.dev.dungcony.modules.product.dtos.res.ProductSummaryRes;
import com.dev.dungcony.modules.product.enums.ProductSize;

public record CartItemDto(
        ProductSummaryRes item,
        ProductSize size,
        Integer quantity
) {
}
