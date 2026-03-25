package com.dev.dungcony.modules.promotion.dtos.res;

import java.time.Instant;

import com.dev.dungcony.modules.promotion.enums.PromotionScope;
import com.dev.dungcony.modules.promotion.enums.PromotionStatus;
import com.dev.dungcony.modules.promotion.enums.PromotionType;

public record PromotionDetailRes(
        PromotionType type,
        String code,
        int value,
        PromotionScope scope,
        Instant startAt,
        Instant endAt,
        Integer priority,
        PromotionStatus status,
        int minPriceApply) {
}
