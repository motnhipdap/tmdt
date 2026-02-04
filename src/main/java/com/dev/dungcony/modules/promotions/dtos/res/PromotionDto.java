package com.dev.dungcony.modules.promotions.dtos.res;

import com.dev.dungcony.modules.promotions.enums.PromotionType;

import java.time.Instant;

public record PromotionDto(
        Integer promotionId,
        PromotionType type,
        int value,
        int minPrice,
        Instant startAt,
        Instant endAt
) {
    public boolean isApplicable(int price) {
        return price >= minPrice && Instant.now().isAfter(startAt) && Instant.now().isBefore(endAt);
    }

}
