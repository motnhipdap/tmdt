package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.DiscountInfo;

public interface PromotionCalculator {
    DiscountInfo calculateFinalPrice(int productId, int categoryId, int price);
}
