package com.dev.dungcony.modules.products.dtos.req;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductAddReq(

        @NotNull Integer categoryId,

        @NotNull Integer providerId,

        @NotBlank String name,

        @NotBlank String productCode,

        String description,

        @NotNull @Positive BigDecimal price,

        @NotNull Integer quantity,

        String imgUrl) {
}