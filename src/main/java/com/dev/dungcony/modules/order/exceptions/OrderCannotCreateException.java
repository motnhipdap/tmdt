package com.dev.dungcony.modules.order.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class OrderCannotCreateException extends AppException {

    public OrderCannotCreateException() {
        super(HttpStatus.BAD_REQUEST, "ORDER_CANNOT_CREATE", "Cannot create order");
    }

    public OrderCannotCreateException(String message) {
        super(HttpStatus.BAD_REQUEST, "ORDER_CANNOT_CREATE", message);
    }
}
