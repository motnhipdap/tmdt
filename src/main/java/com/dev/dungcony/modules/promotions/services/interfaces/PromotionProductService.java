package com.dev.dungcony.modules.promotions.services.interfaces;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import com.dev.dungcony.modules.promotions.entities.Promotion;

import java.util.List;
import java.util.Map;

public interface PromotionProductService {
    List<PromotionDto> getPromotionByProduct(Integer productId);

    /**
     * Batch: lấy promotions cho nhiều products cùng lúc.
     * Key = productId, Value = danh sách PromotionDto
     */
    Map<Integer, List<PromotionDto>> getPromotionsByProducts(List<Integer> productIds);

    void addListPromotionProduct(Promotion promotion, List<Integer> productIds);
}
