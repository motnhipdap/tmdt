package com.dev.dungcony.modules.order.services.interfaces;

import com.dev.dungcony.modules.order.dtos.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.dtos.res.OrderSummaryRes;
import com.dev.dungcony.modules.order.enums.OrderStatus;

import java.util.UUID;

public interface OrderGetService {

    OrderRes getOrderByCode(UUID userId, String orderCode);

    OrderRes getOrderByCodeAdmin(String orderCode);

    OrderDto getDtoByCode(String orderCode);

    Page<OrderSummaryRes> getUserOrders(UUID uId, Pageable pageable);

    Page<OrderSummaryRes> getUserOrdersByStatus(UUID uId, OrderStatus status, Pageable pageable);

    Page<OrderSummaryRes> getAllOrders(Pageable pageable);

    Page<OrderSummaryRes> getAllOrdersByStatus(OrderStatus status, Pageable pageable);
}
