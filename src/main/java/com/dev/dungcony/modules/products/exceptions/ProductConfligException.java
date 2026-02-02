package com.dev.dungcony.modules.products.exceptions;

import com.dev.dungcony.commons.exceptions.ConflictException;

public class ProductConfligException extends ConflictException {
    public ProductConfligException() {
        super("product is already existed");
    }

    public ProductConfligException(String mes) {
        super(mes);
    }

    public ProductConfligException(String code, String message) {
        super(code, message);
    }
}
