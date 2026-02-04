package com.dev.dungcony.modules.promotions.Api;

public interface ApiPromotionService {
    PromotionApiDto getByProductId(int productId, int categoryId, int price);

}
