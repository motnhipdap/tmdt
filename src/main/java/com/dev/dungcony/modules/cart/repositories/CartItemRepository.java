package com.dev.dungcony.modules.cart.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dev.dungcony.modules.cart.entities.CartItem;
import com.dev.dungcony.modules.cart.entities.CartItemId;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {

    @Query("""
            SELECT ci FROM CartItem ci
            JOIN FETCH ci.product p
            JOIN FETCH ci.size s
            WHERE ci.user.id = :userId
            ORDER BY ci.createdAt DESC
            """)
    List<CartItem> findAllByUserId(@Param("userId") UUID userId);

    @Query("""
            SELECT ci FROM CartItem ci
            JOIN FETCH ci.product p
            JOIN FETCH ci.size s
            WHERE ci.user.id = :userId AND ci.isSelected = true
            ORDER BY ci.createdAt DESC
            """)
    List<CartItem> findSelectedByUserId(@Param("userId") UUID userId);

    Optional<CartItem> findById_UserIdAndId_ProductIdAndId_SizeId(
            UUID userId, Integer productId, Integer sizeId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.id.userId = :userId AND ci.id.productId = :productId AND ci.id.sizeId = :sizeId")
    void deleteByCartIdAndProductIdAndSizeId(
            @Param("userId") UUID userId,
            @Param("productId") Integer productId,
            @Param("sizeId") Integer sizeId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.id.userId = :userId")
    void deleteAllByCartId(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE CartItem ci SET ci.isSelected = :selected WHERE ci.id.userId = :userId")
    void updateAllSelectionByCartId(@Param("userId") UUID userId, @Param("selected") boolean selected);
}
