package com.dev.dungcony.modules.promotions.dtos.req;

import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import com.dev.dungcony.modules.promotions.enums.PromotionType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record PromoUpdateReq(
        @NotNull(message = "Promotion ID is required")
        Integer id,

        PromotionType type,

        @Min(value = 0, message = "Value must be non-negative")
        @Max(value = 100, message = "Percent value cannot exceed 100")
        Integer value,

        Instant startAt,

        Instant endAt,

        @Min(value = 0, message = "Priority must be non-negative")
        Integer priority,

        @Min(value = 0, message = "Price requirement must be non-negative")
        Integer priceRequire,

        PromotionStatus status
) {
}

