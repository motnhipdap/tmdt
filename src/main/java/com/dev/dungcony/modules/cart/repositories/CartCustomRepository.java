package com.dev.dungcony.modules.cart.repositories;

import com.dev.dungcony.modules.cart.entities.CartItem;

import java.util.List;
import java.util.UUID;

public interface CartCustomRepository {
    List<CartItem> findAllByUserId(UUID userId);

    List<CartItem> findSelectedByUserId(UUID userId);

    void deleteByCartIdAndProductIdAndSizeId(
            UUID userId,
            Integer productId,
            Integer sizeId);

    void updateAllSelectionByCartId(UUID userId, boolean selected);
}

