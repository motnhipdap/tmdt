package com.dev.dungcony.modules.products.dtos.res;

import com.dev.dungcony.modules.products.enums.ProductStatus;

public record ProductUpdateRes(
        Integer productId,
        Integer categoryId,
        Integer providerId,
        String name,
        String description,
        Double price,
        Float rated,
        Integer quantity,
        Integer quantitySold,
        String img,
        ProductStatus status
) {
}
