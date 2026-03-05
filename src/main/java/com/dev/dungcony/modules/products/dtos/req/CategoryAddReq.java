package com.dev.dungcony.modules.products.dtos.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryAddReq(
        @NotBlank String name,
        @NotNull @NotBlank String code,
        @NotBlank String description,
        String parentCode,
        String imgUrl

) {
}