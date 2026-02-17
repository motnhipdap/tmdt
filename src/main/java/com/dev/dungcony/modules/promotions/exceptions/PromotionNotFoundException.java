package com.dev.dungcony.modules.promotions.exceptions;

public class PromotionNotFoundException extends RuntimeException {
    public PromotionNotFoundException(String message) {
        super(message);
    }

    public PromotionNotFoundException(Integer id) {
        super("Promotion not found with id: " + id);
    }
}

