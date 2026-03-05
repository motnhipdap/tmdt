package com.dev.dungcony.modules.products.dtos.req;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductUpdateReq(
        String productCode,
        String categoryCode,
        String name,
        String description,
        @Positive BigDecimal price,
        @PositiveOrZero Integer quantity,
        String imgUrl) {
}