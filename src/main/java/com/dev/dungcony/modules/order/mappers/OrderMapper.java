package com.dev.dungcony.modules.order.mappers;

import com.dev.dungcony.modules.order.dtos.OrderDto;
import com.dev.dungcony.modules.order.dtos.OrderItemDto;
import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.entities.Order;
import com.dev.dungcony.modules.users.dtos.res.ReceiverRes;

import java.util.List;

public class OrderMapper {
    public static OrderRes toOrderRes(Order order, List<OrderItemDto> items, ReceiverRes reciever) {
        return toOrderRes(order, items, reciever, null);
    }

    public static OrderRes toOrderRes(Order order, List<OrderItemDto> items, ReceiverRes reciever, String paymentUrl) {
        return new OrderRes(
                order.getCode(),
                order.getStatus(),
                order.getPaymentType(),
                order.getVoucherCode(),
                reciever,
                order.getNote(),
                items,
                order.getTotalPrice(),
                order.getVoucherDiscount(),
                order.getFinalPrice(),
                paymentUrl,
                order.getCreatedAt(),
                order.getUpdatedAt());
    }

    public static OrderDto toDto(Order order, List<OrderItemDto> items, String paymentUrl) {
        return new OrderDto(
                order.getId(),
                order.getUserId(),
                order.getCode(),
                order.getStatus(),
                order.getPaymentType(),
                items,
                order.getTotalPrice(),
                order.getVoucherDiscount(),
                order.getFinalPrice(),
                paymentUrl,
                order.getCreatedAt());
    }

}
