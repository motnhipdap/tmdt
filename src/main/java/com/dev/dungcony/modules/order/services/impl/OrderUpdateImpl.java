package com.dev.dungcony.modules.order.services.impl;

import com.dev.dungcony.modules.cart.repositories.CartCustomRepository;
import com.dev.dungcony.modules.order.entities.Order;
import com.dev.dungcony.modules.order.enums.OrderStatus;
import com.dev.dungcony.modules.order.exceptions.OrderConflictException;
import com.dev.dungcony.modules.order.exceptions.OrderNotFoundException;
import com.dev.dungcony.modules.order.mappers.OrderMapper;
import com.dev.dungcony.modules.order.repositories.OrderRepository;
import com.dev.dungcony.modules.order.services.interfaces.OrderUpdateService;
import com.dev.dungcony.modules.users.services.interfaces.RecieverGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderUpdateImpl implements OrderUpdateService {


    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void cancelOrder(UUID userId, String orderCode) {
        Order order = orderRepository.findByCode(orderCode)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.getUserId().equals(userId)) {
            throw new OrderNotFoundException("Order not found");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new OrderConflictException("Only pending orders can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        log.info("Order cancelled: {} by user: {}", orderCode, userId);
    }

    @Override
    public void completedOrder(UUID userId, String orderCode) {
        Order order = orderRepository.findByCode(orderCode)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.getUserId().equals(userId)) {
            throw new OrderNotFoundException("Order not found");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new OrderConflictException("Only pending orders can be cancelled");
        }

        order.setStatus(OrderStatus.CONFIRMED);
        log.info("Order confirm: {} by user: {}", orderCode, userId);
    }


    //---PRIVATE---//
    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        boolean valid = switch (current) {
            case PENDING -> next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELLED;
            case CONFIRMED -> next == OrderStatus.SHIPPING || next == OrderStatus.CANCELLED;
            case SHIPPING -> next == OrderStatus.DELIVERED || next == OrderStatus.RETURNED;
            case DELIVERED -> next == OrderStatus.RETURNED;
            case CANCELLED, RETURNED -> false;
        };

        if (!valid) {
            throw new OrderConflictException(
                    "Cannot transition from " + current + " to " + next);
        }
    }
}
