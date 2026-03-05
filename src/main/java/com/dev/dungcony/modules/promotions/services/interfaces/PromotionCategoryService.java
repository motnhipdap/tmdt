package com.dev.dungcony.modules.promotions.services.interfaces;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionSumaryRes;
import com.dev.dungcony.modules.promotions.entities.Promotion;

import java.util.List;
import java.util.Map;

public interface PromotionCategoryService {
    void addListPromotionCategory(Promotion promotion, List<String> codes);

    List<PromotionSumaryRes> getPromotionByCategory(String code);

    /**
     * Batch: lấy promotions cho nhiều categories cùng lúc.
     * Key = categoryCode, Value = danh sách PromotionDto
     */
    Map<String, List<PromotionSumaryRes>> getPromotionsByCategories(List<String> codes);
}
