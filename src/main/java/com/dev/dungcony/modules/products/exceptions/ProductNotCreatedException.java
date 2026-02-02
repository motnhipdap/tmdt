package com.dev.dungcony.modules.products.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class ProductNotCreatedException extends AppException {
    public ProductNotCreatedException() {
        super(HttpStatus.BAD_REQUEST, "bad_request", "cannot create with category");
    }
}
