package com.dev.dungcony.modules.products.dtos.req;

import com.dev.dungcony.modules.products.enums.ProductStatus;

public record ProductUpdateReq(
        int productId,
        int providerId,
        int categoryId,
        String newName,
        String newDesc,
        int newPrice,
        ProductStatus newStatus
) {
}
