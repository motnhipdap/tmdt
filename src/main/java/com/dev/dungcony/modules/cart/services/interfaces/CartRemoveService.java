package com.dev.dungcony.modules.cart.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.dev.dungcony.modules.cart.dtos.req.AddToCartReq;
import com.dev.dungcony.modules.cart.dtos.req.UpdateCartItemReq;

public interface CartRemoveService {

    void addItemToCart(AddToCartReq req);

    void removeItemFromCart(Integer cartId, String productCode, Integer sizeId);

    void updateItemQuantity(UpdateCartItemReq req);

    void toggleItemSelection(UUID userId, String productCode, Integer sizeId);

    void toggleSelectAll(UUID userId, boolean selected);

    List<CartItemRes> getCartItems(Integer cartId);

    void clearCart(Integer cartId);
}
