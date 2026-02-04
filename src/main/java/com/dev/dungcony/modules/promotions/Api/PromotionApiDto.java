package com.dev.dungcony.modules.promotions.Api;

import java.time.Instant;

public record PromotionApiDto(
        int id,
        String type,
        int value,
        Instant endAt,
        int finalPrice
) {
}
