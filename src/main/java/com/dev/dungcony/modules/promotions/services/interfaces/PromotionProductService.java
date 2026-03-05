package com.dev.dungcony.modules.promotions.services.interfaces;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionSumaryRes;
import com.dev.dungcony.modules.promotions.entities.Promotion;

import java.util.List;
import java.util.Map;

public interface PromotionProductService {
    List<PromotionSumaryRes> getPromotionByProduct(String productCode);

    /**
     * Batch: lấy promotions cho nhiều products cùng lúc.
     * Key = productId, Value = danh sách PromotionDto
     */
    Map<String, List<PromotionSumaryRes>> getPromotionsByProducts(List<String> productCodes);

    void addListPromotionProduct(Promotion promotion, List<String> productCodes);
}
