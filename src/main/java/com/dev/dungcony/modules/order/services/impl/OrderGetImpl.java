package com.dev.dungcony.modules.order.services.impl;

import java.util.List;
import java.util.UUID;

import com.dev.dungcony.modules.product.dtos.res.ProductSummaryRes;
import com.dev.dungcony.modules.product.services.interfaces.product.ProductGetService;
import com.dev.dungcony.modules.users.services.interfaces.RecieverGetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.dtos.res.OrderSummaryRes;
import com.dev.dungcony.modules.order.entities.Order;
import com.dev.dungcony.modules.order.entities.OrderItem;
import com.dev.dungcony.modules.order.enums.OrderStatus;
import com.dev.dungcony.modules.order.exceptions.OrderNotFoundException;
import com.dev.dungcony.modules.order.mappers.OrderMapper;
import com.dev.dungcony.modules.order.repositories.OrderItemRepository;
import com.dev.dungcony.modules.order.repositories.OrderRepository;
import com.dev.dungcony.modules.order.services.interfaces.OrderGetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderGetImpl implements OrderGetService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    private final RecieverGetService recieverGetService;
    private final ProductGetService productGetService;

    @Override
    @Transactional(readOnly = true)
    public OrderRes getOrderByCode(UUID userId, String orderCode) {
        Order order = orderRepository.findByCode(orderCode)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.getUserId().equals(userId)) {
            throw new OrderNotFoundException("Order not found");
        }

        List<OrderItem> items = orderItemRepository.findAllByOrderIdWithDetails(order.getId());

        for (OrderItem item : items) {
            ProductSummaryRes product = productGetService.getByCode()
        }

        return orderMapper.toOrderRes(order, items, recieverGetService.getById(order.getReceiverId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryRes> getUserOrders(UUID userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryRes> getUserOrdersByStatus(UUID userId, OrderStatus status, Pageable pageable) {
        return orderRepository.findAllByUserIdAndStatus(userId, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderRes getOrderByCodeAdmin(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(OrderNotFoundException::new);

        List<OrderItem> items = orderItemRepository.findAllByOrderIdWithDetails(order.getId());
        return orderMapper.toOrderRes(order, items);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryRes> getAllOrders(Pageable pageable) {
        return orderRepository.findAllOrders(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryRes> getAllOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findAllByStatus(status, pageable);
    }
}
