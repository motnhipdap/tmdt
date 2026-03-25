package com.dev.dungcony.modules.cart.services.interfaces;

import com.dev.dungcony.modules.cart.dtos.req.AddToCartReq;
import com.dev.dungcony.modules.cart.dtos.req.RemoveCartItemReq;
import com.dev.dungcony.modules.cart.dtos.req.UpdateCartItemReq;

public interface CartUpdateService {
    void addItemToCart(AddToCartReq req);

    void removeItemFromCart(RemoveCartItemReq req);

    void updateItemQuantity(UpdateCartItemReq req);
}

