package com.dev.dungcony.modules.promotions.dtos.req;

import com.dev.dungcony.modules.promotions.enums.PromotionScope;
import com.dev.dungcony.modules.promotions.enums.PromotionType;

import java.time.Instant;
import java.util.List;

public record PromoAddReq(
        PromotionType type,
        int value,
        PromotionScope scope,
        Instant startAt,
        Instant endAt,
        Integer priority,
        Integer priceRequire,

        List<Integer> productIds,
        List<Integer> categoryIds
) {
}
