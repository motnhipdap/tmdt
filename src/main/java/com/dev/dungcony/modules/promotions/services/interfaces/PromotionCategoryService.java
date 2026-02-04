package com.dev.dungcony.modules.promotions.services.interfaces;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import com.dev.dungcony.modules.promotions.entities.Promotion;

import java.util.List;

public interface PromotionCategoryService {
    void addListPromotionCategory(Promotion promotion, List<Integer> categoryIds);

    List<PromotionDto> getPromotionByCategory(Integer categoryId);
}
