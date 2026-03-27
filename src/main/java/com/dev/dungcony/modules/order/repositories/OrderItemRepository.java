package com.dev.dungcony.modules.order.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dev.dungcony.modules.order.entities.OrderItem;
import com.dev.dungcony.modules.order.entities.OrderItemId;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

    @Query("""
            SELECT oi FROM OrderItem oi
            WHERE oi.order.id = :orderId
            """)
    List<OrderItem> findAllByOrderIdWithDetails(@Param("orderId") Integer orderId);
}
