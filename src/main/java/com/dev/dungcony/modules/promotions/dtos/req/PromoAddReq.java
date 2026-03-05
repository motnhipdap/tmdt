package com.dev.dungcony.modules.promotions.dtos.req;

import com.dev.dungcony.modules.promotions.enums.PromotionScope;
import com.dev.dungcony.modules.promotions.enums.PromotionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

public record PromoAddReq(
        @NotNull(message = "Promotion type is required")
        PromotionType type,

        @NotBlank(message = "Promotion code is required")
        @Size(max = 20, message = "Code must not exceed 20 characters")
        String code,

        @NotNull(message = "Promotion value is required")
        @Min(value = 0, message = "Value must be non-negative")
        Integer value,

        @NotNull(message = "Promotion scope is required")
        PromotionScope scope,

        @NotNull(message = "Start date is required")
        Instant startAt,

        @NotNull(message = "End date is required")
        Instant endAt,

        @Min(value = 0, message = "Priority must be non-negative")
        Integer priority,

        @Min(value = 0, message = "Price requirement must be non-negative")
        Integer priceRequire,

        List<String> productCodes,
        List<String> categoryCodes
) {
}
