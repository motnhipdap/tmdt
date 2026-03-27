package com.dev.dungcony.modules.order.services.impl;

import com.dev.dungcony.modules.order.dtos.OrderItemDto;
import com.dev.dungcony.modules.order.dtos.req.CreateOrderReq;
import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.entities.Order;
import com.dev.dungcony.modules.order.entities.OrderItem;
import com.dev.dungcony.modules.order.entities.OrderItemId;
import com.dev.dungcony.modules.order.enums.OrderStatus;
import com.dev.dungcony.modules.order.exceptions.OrderCannotCreateException;
import com.dev.dungcony.modules.order.exceptions.OrderConflictException;
import com.dev.dungcony.modules.order.mappers.OrderMapper;
import com.dev.dungcony.modules.order.repositories.OrderRepository;
import com.dev.dungcony.modules.order.services.interfaces.OrderCreateService;
import com.dev.dungcony.modules.product.dtos.ProductDto;
import com.dev.dungcony.modules.product.services.interfaces.SizeCacheService;
import com.dev.dungcony.modules.product.services.interfaces.product.ProductGetService;
import com.dev.dungcony.modules.users.dtos.ReceiverDto;
import com.dev.dungcony.modules.users.services.interfaces.RecieverGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreateimpl implements OrderCreateService {

    private final OrderRepository orderRepo;

    private final ProductGetService productGetService;
    private final SizeCacheService sizeCacheService;
    private final RecieverGetService recieverGetService;

    @Override
    @Transactional
    public OrderRes createOrder(UUID userId, CreateOrderReq req) {

        if (req.items() == null || req.items().isEmpty()) {
            throw new OrderCannotCreateException("Order items are required");
        }

        ReceiverDto reciver = recieverGetService.getById(userId, req.recieverid());

        Order order = new Order();
        order.setUserId(userId);
        order.setReceiverId(req.recieverid());
        order.setCode(generateOrderCode());
        order.setStatus(OrderStatus.PENDING);
        order.setNote(req.note());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemDto itemDto : req.items()) {
            if (itemDto.quantity() <= 0) {
                throw new OrderCannotCreateException("Quantity must be greater than 0");
            }

            ProductDto product = productGetService.getDtoByCode(itemDto.productCode());
            int sizeId = sizeCacheService.getIdBySize(itemDto.size());
            OrderItem orderItem = getOrderItem(itemDto, product, sizeId);

            order.addItem(orderItem);

            totalAmount = totalAmount.add(orderItem.getTotaPrice());
        }

        order.setTotalAmount(totalAmount);
        orderRepo.save(order);

        log.info("Order created: {} for user: {}", order.getCode(), userId);

        return OrderMapper.toOrderRes(order, req.items(), reciver);
    }


    //-----PRIVATE-----//
    private String generateOrderCode() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "ORD-" + timestamp.substring(timestamp.length() - 8) + "-" + random;
    }


    private static @NonNull OrderItem getOrderItem(OrderItemDto itemDto, ProductDto product, int sizeId) {
        BigDecimal currentPrice = product.finalPrice();

        if (itemDto.price() == null) {
            throw new OrderCannotCreateException("Item price is required");
        }

        if (itemDto.price().compareTo(currentPrice) != 0) {
            throw new OrderConflictException(
                    "Product price has changed: " + itemDto.productCode());
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setId(new OrderItemId(null, product.id(), sizeId));
        orderItem.setQuantity(itemDto.quantity());
        orderItem.setPrice(currentPrice);
        orderItem.setTotaPrice(currentPrice.multiply(BigDecimal.valueOf(itemDto.quantity())));
        return orderItem;
    }
}
