package com.dev.dungcony.modules.promotions.exceptions;

import com.dev.dungcony.commons.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class InvalidPromotionException extends AppException {
    public InvalidPromotionException(String message) {
        super(HttpStatus.BAD_REQUEST, "INVALID_PROMOTION", message);
    }
}
