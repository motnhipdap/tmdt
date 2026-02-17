package com.dev.dungcony.modules.products.dtos;

public record DiscountInfo(
        Double originalPrice,
        Double finalPrice,
        String discountType,
        int discountValue
) {
}
