package com.dev.dungcony.modules.product.dtos.req;

import java.math.BigDecimal;

import jakarta.validation.constraints.Positive;

public record ProductUpdateReq(
                String productCode,
                String categoryCode,
                String name,
                String description,
                @Positive BigDecimal price,
                String imgUrl) {
}