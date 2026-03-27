package com.dev.dungcony.modules.order.mappers;

import com.dev.dungcony.modules.order.dtos.OrderItemDto;
import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.entities.Order;
import com.dev.dungcony.modules.users.dtos.ReceiverDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    public static OrderRes toOrderRes(Order order, List<OrderItemDto> items, ReceiverDto reciever) {
        return new OrderRes(
                order.getCode(),
                order.getStatus(),
                order.getTotalAmount(),
                reciever,
                order.getNote(),
                items,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
