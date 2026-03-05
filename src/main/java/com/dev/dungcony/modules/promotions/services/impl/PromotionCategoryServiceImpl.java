package com.dev.dungcony.modules.promotions.services.impl;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionSumaryRes;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionCategoryServiceImpl implements PromotionCategoryService {

    private final PromotionCategoryRepository promotionCategoryRepository;

    @Transactional
    @Override
    public void addListPromotionCategory(Promotion promotion, List<String> categoryIds) {
        log.info("Adding {} promotion-category mappings for promotionId={}", categoryIds.size(), promotion.getId());

        List<PromotionCategory> mappings = categoryIds.stream()
                .map(categoryCode -> {
                    PromotionCategory pp = new PromotionCategory();
                    pp.setPromotion(promotion);
                    pp.setId(new PromotionCategoryId(categoryCode, promotion.getId()));
                    return pp;
                })
                .toList();

        promotionCategoryRepository.saveAll(mappings);
    }

    @Override
    public List<PromotionSumaryRes> getPromotionByCategory(String categoryCode) {
        return promotionCategoryRepository.findByCategoryId(categoryCode, Instant.now(), PromotionStatus.ACTIVE);
    }

    @Override
    public Map<Integer, List<PromotionSumaryRes>> getPromotionsByCategories(List<Integer> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Object[]> rows = promotionCategoryRepository
                .findByCategoryIds(categoryIds, Instant.now(), PromotionStatus.ACTIVE);

        return rows.stream()
                .collect(Collectors.groupingBy(
                        row -> (Integer) row[0],
                        Collectors.mapping(row -> (PromotionSumaryRes) row[1], Collectors.toList())
                ));
    }
}
