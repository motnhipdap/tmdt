package com.dev.dungcony.modules.products.dtos.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductBuyReq(
        @NotNull(message = "id is require") Integer id,
        @Min(0) int quantity
) {
}
