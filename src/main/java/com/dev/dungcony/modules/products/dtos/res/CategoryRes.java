package com.dev.dungcony.modules.products.dtos.res;

public record CategoryRes(
        Integer id,
        String name,
        String categoryCode,
        Boolean status,
        String description,
        String path,
        String level,
        String imgUrl) {

}
