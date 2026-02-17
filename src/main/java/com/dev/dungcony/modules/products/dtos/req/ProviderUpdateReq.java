package com.dev.dungcony.modules.products.dtos.req;

public record ProviderUpdateReq(
        Integer providerId,
        String name,
        String email,
        String phone,
        String description,
        String img
) {
}
