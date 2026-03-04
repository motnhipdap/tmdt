package com.dev.dungcony.modules.products.dtos.res;

import com.dev.dungcony.modules.products.enums.CategoryStatus;

public record CategoryRes(
        Integer id,
        String name,
        String categoryCode,
        CategoryStatus status,
        String description,
        String path,
        Integer level,
        String imgUrl) {

}
