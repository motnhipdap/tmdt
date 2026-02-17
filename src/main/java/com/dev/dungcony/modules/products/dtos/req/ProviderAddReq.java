package com.dev.dungcony.modules.products.dtos.req;

public record ProviderAddReq(
        String name,
        String email,
        String phone,
        String description,
        String img
) {
}
