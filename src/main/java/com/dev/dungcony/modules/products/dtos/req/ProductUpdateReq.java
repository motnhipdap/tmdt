package com.dev.dungcony.modules.products.dtos.req;

import java.math.BigDecimal;

public record ProductUpdateReq(
        Integer categoryId,
        Integer providerId,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        String imgUrl) {
}