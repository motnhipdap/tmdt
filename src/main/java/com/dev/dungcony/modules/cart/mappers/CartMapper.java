package com.dev.dungcony.modules.cart.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dev.dungcony.modules.cart.entities.CartItem;

@Component
public class CartMapper {

    public CartItemRes toCartItemRes(CartItem item) {
        return new CartItemRes(
                item.getProduct().getCode(),
                item.getProduct().getName(),
                item.getProduct().getImg(),
                item.getSize().getId(),
                item.getSize().getSize() != null ? item.getSize().getSize().name() : null,
                item.getProduct().getPrice(),
                item.getQuantity(),
                item.getIsSelected());
    }

    public CartRes toCartRes(List<CartItem> items) {
        List<CartItemRes> itemResList = items.stream()
                .map(this::toCartItemRes)
                .toList();
        return new CartRes(itemResList);
    }
}
