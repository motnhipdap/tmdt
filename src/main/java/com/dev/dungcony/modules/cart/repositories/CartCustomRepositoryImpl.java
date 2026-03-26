package com.dev.dungcony.modules.cart.repositories;

import com.dev.dungcony.modules.cart.entities.CartItem;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CartCustomRepositoryImpl implements CartCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<CartItem> findAllByUserId(UUID userId) {
        return entityManager.createQuery("""
                        SELECT ci FROM CartItem ci
                        JOIN FETCH ci.product
                        JOIN FETCH ci.size
                        WHERE ci.user.id = :userId
                        ORDER BY ci.createdAt DESC
                        """, CartItem.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<CartItem> findSelectedByUserId(UUID userId) {
        return entityManager.createQuery("""
                        SELECT ci FROM CartItem ci
                        JOIN FETCH ci.product
                        JOIN FETCH ci.size
                        WHERE ci.user.id = :userId AND ci.isSelected = true
                        ORDER BY ci.createdAt DESC
                        """, CartItem.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void deleteByCartIdAndProductIdAndSizeId(UUID userId, Integer productId, Integer sizeId) {
        entityManager.createQuery("""
                        DELETE FROM CartItem ci
                        WHERE ci.id.userId = :userId
                          AND ci.id.productId = :productId
                          AND ci.id.sizeId = :sizeId
                        """)
                .setParameter("userId", userId)
                .setParameter("productId", productId)
                .setParameter("sizeId", sizeId)
                .executeUpdate();
    }

    @Override
    public void updateAllSelectionByCartId(UUID userId, boolean selected) {
        entityManager.createQuery("""
                        UPDATE CartItem ci
                        SET ci.isSelected = :selected
                        WHERE ci.id.userId = :userId
                        """)
                .setParameter("userId", userId)
                .setParameter("selected", selected)
                .executeUpdate();
    }
}
