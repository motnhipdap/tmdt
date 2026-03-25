package com.dev.dungcony.modules.order.services.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.dtos.res.OrderSummaryRes;
import com.dev.dungcony.modules.order.enums.OrderStatus;

public interface OrderGetService {

    OrderRes getOrderByCode(UUID userId, String orderCode);

    Page<OrderSummaryRes> getUserOrders(UUID userId, Pageable pageable);

    Page<OrderSummaryRes> getUserOrdersByStatus(UUID userId, OrderStatus status, Pageable pageable);

    OrderRes getOrderByCodeAdmin(String orderCode);

    Page<OrderSummaryRes> getAllOrders(Pageable pageable);

    Page<OrderSummaryRes> getAllOrdersByStatus(OrderStatus status, Pageable pageable);
}
