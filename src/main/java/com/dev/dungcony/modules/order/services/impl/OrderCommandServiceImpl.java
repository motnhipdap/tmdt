package com.dev.dungcony.modules.order.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.dev.dungcony.modules.cart.repositories.CartCustomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.dungcony.modules.cart.entities.CartItem;
import com.dev.dungcony.modules.order.dtos.req.CreateOrderReq;
import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.entities.Order;
import com.dev.dungcony.modules.order.entities.OrderItem;
import com.dev.dungcony.modules.order.entities.OrderItemId;
import com.dev.dungcony.modules.order.enums.OrderStatus;
import com.dev.dungcony.modules.order.exceptions.OrderCannotCreateException;
import com.dev.dungcony.modules.order.exceptions.OrderConflictException;
import com.dev.dungcony.modules.order.exceptions.OrderNotFoundException;
import com.dev.dungcony.modules.order.mappers.OrderMapper;
import com.dev.dungcony.modules.order.repositories.OrderRepository;
import com.dev.dungcony.modules.order.services.interfaces.OrderCommandService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;
    private final CartCustomRepository cartCustomRepo;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderRes createOrderFromCart(UUID userId, CreateOrderReq req) {
        List<CartItem> selectedItems = cartCustomRepo.findSelectedByUserId(userId);

        if (selectedItems.isEmpty()) {
            throw new OrderCannotCreateException("No items selected in cart");
        }

        Order order = new Order();
        order.setOrderCode(generateOrderCode());
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(req.shippingAddress());
        order.setPhone(req.phone());
        order.setReceiverName(req.receiverName());
        order.setNote(req.note());

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : selectedItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(new OrderItemId(null, cartItem.getProduct().getId(), cartItem.getSize().getId()));
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setSize(cartItem.getSize());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setProductName(cartItem.getProduct().getName());
            orderItem.setProductImg(cartItem.getProduct().getImg());
            orderItem.setSizeName(cartItem.getSize().getSize() != null
                    ? cartItem.getSize().getSize().name()
                    : null);

            BigDecimal itemTotal = cartItem.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);
        orderRepository.save(order);

        for (CartItem cartItem : selectedItems) {
            cartCustomRepo.deleteByCartIdAndProductIdAndSizeId(
                    userId,
                    cartItem.getProduct().getId(),
                    cartItem.getSize().getId());
        }

        log.info("Order created: {} for user: {}", order.getOrderCode(), userId);
        return orderMapper.toOrderRes(order);
    }

    @Override
    @Transactional
    public void cancelOrder(UUID userId, String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
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
    @Transactional
    public void updateOrderStatus(String orderCode, OrderStatus status) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(OrderNotFoundException::new);

        validateStatusTransition(order.getStatus(), status);
        order.setStatus(status);
        log.info("Order {} status updated to {}", orderCode, status);
    }

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

    private String generateOrderCode() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "ORD-" + timestamp.substring(timestamp.length() - 8) + "-" + random;
    }
}
