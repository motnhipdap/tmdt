package com.dev.dungcony.modules.cart.mappers;

import com.dev.dungcony.modules.cart.dtos.res.CartItemRes;
import com.dev.dungcony.modules.cart.dtos.res.CartRes;
import com.dev.dungcony.modules.cart.entities.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartItemRes toCartItemRes(CartItem item) {
        BigDecimal unitPrice = item.getProduct().getPrice();
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

        return new CartItemRes(
                item.getProduct().getCode(),
                item.getProduct().getName(),
                item.getProduct().getImg(),
                item.getSize().getId(),
                item.getSize().getSize(),
                unitPrice,
                item.getQuantity(),
                lineTotal);
    }

    public CartRes toCartRes(List<CartItem> items) {
        List<CartItemRes> itemResList = items.stream()
                .map(this::toCartItemRes)
                .toList();

        BigDecimal totalAmount = itemResList.stream()
                .map(CartItemRes::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartRes(itemResList, itemResList.size(), totalAmount);
    }
}
