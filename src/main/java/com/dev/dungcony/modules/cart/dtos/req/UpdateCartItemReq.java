package com.dev.dungcony.modules.cart.dtos.req;

import com.dev.dungcony.modules.product.enums.ProductSize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateCartItemReq(
        @NotNull UUID id,
        @NotBlank String productCode,
        @NotNull ProductSize size,
        @NotNull @Min(1) Integer quantity) {
}
