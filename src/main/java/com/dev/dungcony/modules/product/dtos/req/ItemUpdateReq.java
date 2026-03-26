package com.dev.dungcony.modules.product.dtos.req;

import com.dev.dungcony.modules.product.enums.ItemStatus;

public record ItemUpdateReq(
        Integer productId,
        Integer sizeId,
        Integer quantity,
        ItemStatus status
) {
}
