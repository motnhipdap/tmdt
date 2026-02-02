package com.dev.dungcony.modules.products.dtos.res;

public record ProductBasicDto(
        Integer id,
        String name,
        int price,
        float rated,
        String image
) {
}
