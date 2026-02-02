package com.dev.dungcony.modules.products.dtos.req;

public record ProductAddReq(
        Integer categoryId,
        Integer providerId,
        String name,
        String description,
        Integer price,
        Integer quantity
) {
}
