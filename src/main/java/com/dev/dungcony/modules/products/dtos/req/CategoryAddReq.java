package com.dev.dungcony.modules.products.dtos.req;

public record CategoryAddReq(
                String name,
                String categoryCode,
                String description,
                Integer parent_id,
                String img) {
}
