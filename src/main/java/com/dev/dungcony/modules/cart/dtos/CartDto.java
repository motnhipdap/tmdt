package com.dev.dungcony.modules.cart.dtos;

import java.util.List;
import java.util.UUID;

public record CartDto(
        UUID userId,
        List<CartItemDto> items
) {
}
