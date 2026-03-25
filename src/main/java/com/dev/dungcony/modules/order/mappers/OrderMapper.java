package com.dev.dungcony.modules.order.mappers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.dev.dungcony.modules.order.dtos.res.OrderItemRes;
import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.entities.Order;
import com.dev.dungcony.modules.order.entities.OrderItem;

@Component
public class OrderMapper {

    public OrderItemRes toOrderItemRes(OrderItem item) {
        BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return new OrderItemRes(
                item.getProduct() != null ? item.getProduct().getCode() : null,
                item.getProductName(),
                item.getProductImg(),
                item.getSizeName(),
                item.getQuantity(),
                item.getPrice(),
                subtotal);
    }

    public OrderRes toOrderRes(Order order) {
        List<OrderItemRes> itemResList = order.getItems().stream()
                .map(this::toOrderItemRes)
                .toList();

        return new OrderRes(
                order.getOrderCode(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getShippingAddress(),
                order.getPhone(),
                order.getReceiverName(),
                order.getNote(),
                itemResList,
                order.getCreatedAt(),
                order.getUpdatedAt());
    }

    public OrderRes toOrderRes(Order order, List<OrderItem> items) {
        List<OrderItemRes> itemResList = items.stream()
                .map(this::toOrderItemRes)
                .toList();

        return new OrderRes(
                order.getOrderCode(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getShippingAddress(),
                order.getPhone(),
                order.getReceiverName(),
                order.getNote(),
                itemResList,
                order.getCreatedAt(),
                order.getUpdatedAt());
    }
}
