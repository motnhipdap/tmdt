package com.dev.dungcony.modules.promotions.services.impl;

import com.dev.dungcony.modules.products.dtos.DiscountInfoDto;
import com.dev.dungcony.modules.products.services.interfaces.PromotionCalculator;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionCategoryService;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionProductService;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

/**
 * Service chuyên trách tính toán giá sau promotion.
 * Tách riêng khỏi PromotionServiceImpl để tuân thủ SRP (Single Responsibility Principle).
 * <p>
 * Delegate query xuống service layer (PromotionProductService, PromotionCategoryService, PromotionService)
 * thay vì gọi trực tiếp repository — đảm bảo single source of truth.
 * <p>
 * Hỗ trợ cả tính đơn lẻ (1 product) và batch (nhiều products) để tránh N+1 query.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionCalculatorImpl implements PromotionCalculator {

    private final PromotionProductService promotionProductService;
    private final PromotionCategoryService promotionCategoryService;
    private final PromotionService promotionService;

    @Override
    public DiscountInfoDto calculateFinalPrice(int productId, int categoryId, BigDecimal price) {
        log.debug("Calculating final price for productId={}, categoryId={}, price={}", productId, categoryId, price);

        Instant now = Instant.now();

        // Delegate sang service layer — single source of truth
        List<PromotionDto> productPromotions = promotionProductService.getPromotionByProduct(productId);
        List<PromotionDto> categoryPromotions = promotionCategoryService.getPromotionByCategory(categoryId);
        List<PromotionDto> globalPromotions = promotionService.getGlobalPromotions(now);

        return findBestDiscount(price, now, productPromotions, categoryPromotions, globalPromotions);
    }

    @Override
    public Map<Integer, DiscountInfoDto> calculateFinalPrices(List<ProductPriceInput> inputs) {
        if (inputs == null || inputs.isEmpty()) {
            return Collections.emptyMap();
        }

        Instant now = Instant.now();

        // Thu thập tất cả productIds và categoryIds để batch query
        List<Integer> productIds = inputs.stream().map(ProductPriceInput::productId).toList();
        List<Integer> categoryIds = inputs.stream().map(ProductPriceInput::categoryId).distinct().toList();

        // Batch query qua service layer: 3 queries thay vì 3*N
        Map<Integer, List<PromotionDto>> productPromotionMap =
                promotionProductService.getPromotionsByProducts(productIds);
        Map<Integer, List<PromotionDto>> categoryPromotionMap =
                promotionCategoryService.getPromotionsByCategories(categoryIds);
        List<PromotionDto> globalPromotions = promotionService.getGlobalPromotions(now);

        // Tính cho từng product
        Map<Integer, DiscountInfoDto> result = new HashMap<>();
        for (ProductPriceInput input : inputs) {
            List<PromotionDto> prodPromos = productPromotionMap.getOrDefault(input.productId(), List.of());
            List<PromotionDto> catePromos = categoryPromotionMap.getOrDefault(input.categoryId(), List.of());

            DiscountInfoDto discount = findBestDiscount(input.price(), now, prodPromos, catePromos, globalPromotions);
            result.put(input.productId(), discount);
        }

        return result;
    }

    // ============ PRIVATE HELPERS ============

    /**
     * Tìm promotion tốt nhất (giảm nhiều nhất) từ tất cả các nguồn.
     */
    private DiscountInfoDto findBestDiscount(
            BigDecimal price,
            Instant now,
            List<PromotionDto> productPromotions,
            List<PromotionDto> categoryPromotions,
            List<PromotionDto> globalPromotions
    ) {
        // Gộp tất cả promotions và lọc applicable
        List<PromotionDto> allPromotions = new ArrayList<>(
                productPromotions.size() + categoryPromotions.size() + globalPromotions.size()
        );
        allPromotions.addAll(productPromotions);
        allPromotions.addAll(categoryPromotions);
        allPromotions.addAll(globalPromotions);

        List<PromotionDto> applicablePromotions = allPromotions.stream()
                .filter(promo -> promo.isApplicable(price, now))
                .toList();

        if (applicablePromotions.isEmpty()) {
            return DiscountInfoDto.noDiscount(price);
        }

        // Tìm promotion cho discount cao nhất
        PromotionDto bestPromotion = applicablePromotions.stream()
                .max(Comparator.comparing(p -> p.calculateDiscount(price)))
                .orElseThrow();

        BigDecimal discount = bestPromotion.calculateDiscount(price);
        BigDecimal finalPrice = price.subtract(discount);

        log.debug("Best promotion: type={}, value={}, discount={}, finalPrice={}",
                bestPromotion.type(), bestPromotion.value(), discount, finalPrice);

        return new DiscountInfoDto(price, finalPrice, bestPromotion.type().getValue(), bestPromotion.value());
    }
}
