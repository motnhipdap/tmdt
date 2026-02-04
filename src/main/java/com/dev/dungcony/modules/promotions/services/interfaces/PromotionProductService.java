package com.dev.dungcony.modules.promotions.services.interfaces;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import com.dev.dungcony.modules.promotions.entities.Promotion;

import java.util.List;

public interface PromotionProductService {
    List<PromotionDto> getPromotionByProduct(Integer productId);

    void addListPromotionProduct(Promotion promotion, List<Integer> productIds);
}
