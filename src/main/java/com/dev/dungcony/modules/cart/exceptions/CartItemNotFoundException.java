package com.dev.dungcony.modules.cart.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class CartItemNotFoundException extends AppException {

    public CartItemNotFoundException() {
        super(HttpStatus.NOT_FOUND, "CART_ITEM_NOT_FOUND", "Cart item not found");
    }

    public CartItemNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "CART_ITEM_NOT_FOUND", message);
    }
}
