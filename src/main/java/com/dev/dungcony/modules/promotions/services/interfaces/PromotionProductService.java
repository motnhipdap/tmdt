package com.dev.dungcony.modules.promotions.services.interfaces;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionSumaryRes;
import com.dev.dungcony.modules.promotions.entities.Promotion;

import java.util.List;
import java.util.Map;

public interface PromotionProductService {
    List<PromotionSumaryRes> getPromotionByProduct(Integer productId);

    /**
     * Batch: lấy promotions cho nhiều products cùng lúc.
     * Key = productId, Value = danh sách PromotionDto
     */
    Map<Integer, List<PromotionSumaryRes>> getPromotionsByProducts(List<Integer> productIds);

    void addListPromotionProduct(Promotion promotion, List<Integer> productIds);
}
