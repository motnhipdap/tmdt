package com.dev.dungcony.modules.promotion.dtos.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

import com.dev.dungcony.modules.promotion.enums.PromotionStatus;
import com.dev.dungcony.modules.promotion.enums.PromotionType;

public record PromoUpdateReq(
        @NotBlank(message = "Promotion code is required") String code,

        PromotionType type,

        @Min(value = 0, message = "Value must be non-negative") Integer value,

        Instant startAt,

        Instant endAt,

        @Min(value = 0, message = "Priority must be non-negative") Integer priority,

        @Min(value = 0, message = "Price requirement must be non-negative") Integer priceRequire,

        PromotionStatus status) {
}
