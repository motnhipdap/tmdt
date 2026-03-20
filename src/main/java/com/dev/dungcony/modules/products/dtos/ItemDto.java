package com.dev.dungcony.modules.products.dtos;

import com.dev.dungcony.modules.products.enums.ItemStatus;
import com.dev.dungcony.modules.products.enums.ProductSize;

public record ItemDto(
                ProductSize size,
                ItemStatus status,
                Integer quantity) {
}
