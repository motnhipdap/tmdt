package com.dev.dungcony.modules.cart.services.interfaces;

import com.dev.dungcony.modules.cart.dtos.req.AddToCartReq;
import com.dev.dungcony.modules.cart.dtos.req.RemoveCartItemReq;
import com.dev.dungcony.modules.cart.dtos.req.UpdateCartItemReq;
import com.dev.dungcony.modules.product.enums.ProductSize;

import java.util.UUID;

public interface CartUpdateService {
    void addItemToCart(AddToCartReq req);

    void removeItemFromCart(UUID userId, String productCode, ProductSize size);

    void updateItemQuantity(UpdateCartItemReq req);

    void clearCart(UUID userId);
}

