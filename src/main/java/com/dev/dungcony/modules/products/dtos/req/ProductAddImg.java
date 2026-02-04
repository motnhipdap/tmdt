package com.dev.dungcony.modules.products.dtos.req;

import jakarta.validation.constraints.NotNull;

public record ProductAddImg(
        @NotNull(message = "Product ID is required") Integer productId,
        @NotNull(message = "Image URL is required") String imgUrl
) {
}
