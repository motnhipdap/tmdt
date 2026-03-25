package com.dev.dungcony.modules.promotion.dtos;

import java.math.BigDecimal;

import com.dev.dungcony.modules.product.enums.ProductSize;

public record IteamCartDto(
        String productCode,
        String productName,
        String urlImage,
        BigDecimal price,
        ProductSize size,
        Integer quantity) {

}
