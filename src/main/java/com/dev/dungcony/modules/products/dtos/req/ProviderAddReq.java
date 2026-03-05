package com.dev.dungcony.modules.products.dtos.req;

import jakarta.validation.constraints.NotBlank;

public record ProviderAddReq(
        @NotBlank String name,
        String providerCode,
        String email,
        String phone,
        String description,
        String img) {
}
