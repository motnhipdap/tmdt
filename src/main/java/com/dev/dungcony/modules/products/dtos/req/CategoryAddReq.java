package com.dev.dungcony.modules.products.dtos.req;

import jakarta.validation.constraints.NotBlank;

public record CategoryAddReq(
        @NotBlank
        String name,
        @NotBlank
        String categoryCode,
        String description,
        String parentCode,
        String imgUrl

) {
}