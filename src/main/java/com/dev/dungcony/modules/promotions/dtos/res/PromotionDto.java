package com.dev.dungcony.modules.promotions.dtos.res;

import com.dev.dungcony.modules.promotions.enums.PromotionType;

import java.math.BigDecimal;
import java.time.Instant;

public record PromotionDto(
        Integer promotionId,
        PromotionType type,
        int value,
        BigDecimal minPrice,
        Instant startAt,
        Instant endAt
) {
    /**
     * Kiểm tra promotion có áp dụng được cho giá sản phẩm và thời điểm hiện tại không.
     * Sử dụng timestamp truyền vào thay vì gọi Instant.now() nhiều lần để đảm bảo tính nhất quán.
     */
    public boolean isApplicable(BigDecimal price, Instant now) {
        return price.compareTo(minPrice) >= 0
                && now.isAfter(startAt)
                && now.isBefore(endAt);
    }

    /**
     * Tính số tiền được giảm dựa trên loại promotion.
     */
    public BigDecimal calculateDiscount(BigDecimal price) {
        return switch (type) {
            case PERCENT -> price.multiply(BigDecimal.valueOf(value))
                    .divide(BigDecimal.valueOf(100), 0, java.math.RoundingMode.HALF_UP);
            case FIXED -> BigDecimal.valueOf(value).min(price); // Không giảm quá giá sản phẩm
        };
    }
}
