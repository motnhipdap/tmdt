package com.dev.dungcony.modules.products.dtos.res;

public record ProductBasicDto(
        Integer id,
        String name,
        int originalPrice,
        int finalPrice,
        float rated,
        String image
) {
}
