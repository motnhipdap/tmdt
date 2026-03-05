package com.dev.dungcony.modules.products.dtos.res;

import com.dev.dungcony.modules.products.enums.CategoryStatus;

public record CategoryRes(
        String name,
        String categoryCode,
        CategoryStatus status,
        String description,
        String imgUrl) {

}
