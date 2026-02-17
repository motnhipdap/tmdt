package com.dev.dungcony.modules.products.dtos.res;

public record ProductAddRes(
        Integer id,
        String name,
        String description,
        double price,
        int quantity,
        String image
) {
}
