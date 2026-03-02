package com.dev.dungcony.modules.products.dtos;

import java.math.BigDecimal;

public record DiscountDto(
                BigDecimal originalPrice,
                BigDecimal finalPrice,
                String discountType,
                int discountValue) {
}
