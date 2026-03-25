package com.dev.dungcony.modules.cart.dtos.req;

import com.dev.dungcony.modules.product.enums.ProductSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RemoveCartItemReq(
        UUID userId,
        @NotBlank String productCode,
        @NotNull ProductSize size) {
}
