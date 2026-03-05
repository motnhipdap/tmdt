package com.dev.dungcony.modules.products.dtos.req;

import com.dev.dungcony.modules.products.enums.CategoryStatus;

public record CategoryUpdateReq(
        String name,
        String description,
        String imgUrl,
        CategoryStatus status
) {
}