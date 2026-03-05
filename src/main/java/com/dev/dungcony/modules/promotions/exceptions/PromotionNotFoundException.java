package com.dev.dungcony.modules.promotions.exceptions;

import com.dev.dungcony.commons.exceptions.NotFoundException;

public class PromotionNotFoundException extends NotFoundException {
    public PromotionNotFoundException(String message) {
        super("PROMOTION_NOT_FOUND", message);
    }

    public PromotionNotFoundException(Integer id) {
        super("PROMOTION_NOT_FOUND", "Promotion not found with id: " + id);
    }
}
