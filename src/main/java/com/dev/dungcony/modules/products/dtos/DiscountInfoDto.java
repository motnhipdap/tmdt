package com.dev.dungcony.modules.products.dtos;

import java.math.BigDecimal;

/**
 * Kết quả tính giá sau khi áp dụng promotion tốt nhất.
 */
public record DiscountInfoDto(
        BigDecimal originalPrice,
        BigDecimal finalPrice,
        String discountType,
        int discountValue
) {
    public static DiscountInfoDto noDiscount(BigDecimal price) {
        return new DiscountInfoDto(price, price, "NONE", 0);
    }
}

