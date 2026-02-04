package com.dev.dungcony.modules.promotions.Api;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionCategoryService;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ApiPromotionServiceImpl implements ApiPromotionService {

    private final PromotionCategoryService promotionCategoryService;
    private final PromotionProductService promotionProductService;

    @Override
    public PromotionApiDto getByProductId(int productId, int categoryId, int price) {

        // 1️⃣ Ưu tiên PRODUCT
        PromotionApiDto dto = buildBestPromotion(
                promotionProductService.getPromotionByProduct(productId),
                price
        );
        if (dto != null) return dto;

        // 2️⃣ Fallback CATEGORY
        return buildBestPromotion(
                promotionCategoryService.getPromotionByCategory(categoryId),
                price
        );
    }


    private int applyPromotion(int price, PromotionDto p) {
        return switch (p.type()) {
            case PERCENT -> Math.max(0, price - (price * p.value() / 100));
            case FIXED -> Math.max(0, price - p.value());
        };
    }

    private PromotionApiDto buildBestPromotion(
            List<PromotionDto> promotions,
            int price
    ) {
        return promotions.stream()
                .filter(p -> p.isApplicable(price))
                .min(Comparator.comparingInt(p -> applyPromotion(price, p)))
                .map(p -> new PromotionApiDto(
                        p.promotionId(),
                        p.type().getValue(),
                        p.value(),
                        p.endAt(),
                        applyPromotion(price, p)
                ))
                .orElse(null);
    }

}
