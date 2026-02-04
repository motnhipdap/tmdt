package com.dev.dungcony.modules.promotions.services.impl;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import com.dev.dungcony.modules.promotions.entities.Promotion;
import com.dev.dungcony.modules.promotions.entities.PromotionCategory;
import com.dev.dungcony.modules.promotions.entities.PromotionCategoryId;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import com.dev.dungcony.modules.promotions.reporitories.PromotionCategoryRepository;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionCategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionCategoryServiceImpl implements PromotionCategoryService {

    private final PromotionCategoryRepository promotionCategoryRepository;

    @Transactional
    @Override
    public void addListPromotionCategory(Promotion promotion, List<Integer> categoryIds) {

        log.info("adding list promotion category.....");

        List<PromotionCategory> mappings = categoryIds.stream()
                .map(categoryId -> {
                    PromotionCategory pp = new PromotionCategory();
                    pp.setPromotion(promotion);
                    pp.setId(new PromotionCategoryId(categoryId, promotion.getId()));
                    return pp;
                })
                .toList();

        promotionCategoryRepository.saveAll(mappings);
    }

    @Override
    public List<PromotionDto> getPromotionByCategory(Integer categoryId) {
        return promotionCategoryRepository.findByCategoryId(categoryId, Instant.now(), PromotionStatus.ACTIVE);
    }
}
