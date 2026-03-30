package com.dev.dungcony.modules.order.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dev.dungcony.modules.order.entities.OrderItem;
import com.dev.dungcony.modules.order.entities.OrderItemId;
import com.dev.dungcony.modules.order.enums.OrderStatus;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

    @Query("""
            SELECT oi FROM OrderItem oi
            WHERE oi.order.id = :orderId
            """)
    List<OrderItem> findAllByOrderIdWithDetails(@Param("orderId") Integer orderId);

    @Query("""
            SELECT CASE WHEN COUNT(oi) > 0 THEN true ELSE false END
            FROM OrderItem oi
            JOIN oi.order o
            WHERE o.userId = :userId
              AND oi.id.productId = :productId
              AND o.status = :status
            """)
    boolean existsByUserIdAndProductIdAndOrderStatus(
            @Param("userId") UUID userId,
            @Param("productId") Integer productId,
            @Param("status") OrderStatus status);
}
