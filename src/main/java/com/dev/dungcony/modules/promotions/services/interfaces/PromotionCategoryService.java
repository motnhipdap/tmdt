package com.dev.dungcony.modules.promotions.services.interfaces;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionSummaryRes;
import com.dev.dungcony.modules.promotions.entities.Promotion;

import java.util.List;
import java.util.Map;

public interface PromotionCategoryService {
    void addListPromotionCategory(Promotion promotion, List<String> codes);

    List<PromotionSummaryRes> getPromotionByCategory(String code);

    /**
     * Batch: lấy promotions cho nhiều categories cùng lúc.
     * Key = categoryCode, Value = danh sách PromotionDto
     */
    Map<String, List<PromotionSummaryRes>> getPromotionsByCategories(List<String> codes);
}
