package com.dev.dungcony.modules.promotions.dtos.res;

import com.dev.dungcony.modules.promotions.enums.PromotionScope;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import com.dev.dungcony.modules.promotions.enums.PromotionType;

import java.time.Instant;

public record PromotionDetailDto(
        Integer id,
        PromotionType type,
        int value,
        PromotionScope scope,
        Instant startAt,
        Instant endAt,
        Integer priority,
        PromotionStatus status,
        int minPriceApply
) {
}

