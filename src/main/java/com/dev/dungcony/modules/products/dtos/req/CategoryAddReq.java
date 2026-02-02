package com.dev.dungcony.modules.products.dtos.req;

public record CategoryAddReq(
        String name,
        String description,
        String img,
        Integer parent_id
) {
}
