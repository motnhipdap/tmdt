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
                order.getTotalAmount(),
                reciever,
                order.getNote(),
                items,
                order.getCreatedAt(),
                order.getUpdatedAt());
    }
}
