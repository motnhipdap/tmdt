package com.dev.dungcony.modules.cart.repositories;

import com.dev.dungcony.modules.cart.entities.CartItem;
import com.dev.dungcony.modules.cart.entities.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartItem, CartItemId>, CartCustomRepository {

    Optional<CartItem> findById_UserIdAndId_ProductIdAndId_SizeId(
            UUID userId, Integer productId, Integer sizeId);

    void deleteAllById_UserId(UUID userId);
}
