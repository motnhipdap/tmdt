package com.dev.dungcony.modules.products.dtos.res;

public record ProviderUpdateRes(
        Integer providerId,
        String name,
        String email,
        String phone,
        String description,
        String img
) {
}
