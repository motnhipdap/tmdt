package com.dev.dungcony.modules.order.mappers;

import com.dev.dungcony.modules.order.dtos.OrderItemDto;
import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.entities.Order;
import com.dev.dungcony.modules.users.dtos.res.ReceiverRes;

import java.util.List;

public class OrderMapper {
    public static OrderRes toOrderRes(Order order, List<OrderItemDto> items, ReceiverRes reciever) {
        return new OrderRes(
                order.getCode(),
                order.getStatus(),
                order.getVoucherCode(),
                reciever,
                order.getNote(),
                items,
                order.getTotalPrice(),
                order.getVoucherDiscount(),
                order.getFinalPrice(),
                order.getCreatedAt(),
                order.getUpdatedAt());
    }
}
