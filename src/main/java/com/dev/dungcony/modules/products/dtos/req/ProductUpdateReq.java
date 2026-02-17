package com.dev.dungcony.modules.products.dtos.req;

public record ProductUpdateReq(
        Integer productId,
        Integer newProviderId,
        Integer newCategoryId,
        String newName,
        String newDesc,
        Double newPrice,
        Integer newQuantity,
        Integer newSold
) {
}
