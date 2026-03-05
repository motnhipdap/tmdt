package com.dev.dungcony.modules.promotions.services.impl;

import com.dev.dungcony.modules.products.dtos.DiscountInfoDto;
import com.dev.dungcony.modules.products.services.interfaces.PromotionCalculator;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionSumaryRes;
import com.dev.dungcony.modules.promotions.services.interfaces.GetIdByCode;
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
    private final GetIdByCode getIdByCode;

    @Override
    public DiscountInfoDto calculateFinalPrice(String productCode, String categoryCode, BigDecimal price) {
        log.debug("Calculating final price for productCode={}, categoryCode={}, price={}", productCode, categoryCode, price);

        Instant now = Instant.now();

        // Delegate sang service layer — single source of truth
        List<PromotionSumaryRes> productPromotions = promotionProductService.getPromotionByProduct(productCode);
        List<PromotionSumaryRes> categoryPromotions = promotionCategoryService.getPromotionByCategory(categoryCode);
        List<PromotionSumaryRes> globalPromotions = promotionService.getGlobalPromotions(now);

        return findBestDiscount(price, now, productPromotions, categoryPromotions, globalPromotions);
    }

    @Override
    public Map<Integer, DiscountInfoDto> calculateFinalPrices(List<ProductPriceInput> inputs) {
        if (inputs == null || inputs.isEmpty()) {
            return Collections.emptyMap();
        }

        Instant now = Instant.now();

        // Thu thập tất cả productIds và categoryIds để batch query
        List<String> productCodes = inputs.stream().map(ProductPriceInput::productCode).toList();
        List<String> categoryCodes = inputs.stream().map(ProductPriceInput::categoryCode).distinct().toList();

        // Batch query qua service layer: 3 queries thay vì 3*N
        Map<Integer, List<PromotionSumaryRes>> productPromotionMap =
                promotionProductService.getPromotionsByProducts(productCodes);
        Map<Integer, List<PromotionSumaryRes>> categoryPromotionMap =
                promotionCategoryService.getPromotionsByCategories(categoryCodes);
        List<PromotionSumaryRes> globalPromotions = promotionService.getGlobalPromotions(now);

        // Tính cho từng product
        Map<Integer, DiscountInfoDto> result = new HashMap<>();
        for (ProductPriceInput input : inputs) {
            List<PromotionSumaryRes> prodPromos = productPromotionMap.getOrDefault(getIdByCode.getByCategoryCode(input.productCode()), List.of());
            List<PromotionSumaryRes> catePromos = categoryPromotionMap.getOrDefault(getIdByCode.getByCategoryCode(input.categoryCode()), List.of());

            DiscountInfoDto discount = findBestDiscount(input.price(), now, prodPromos, catePromos, globalPromotions);
            result.put(getIdByCode.getByCategoryCode(input.productCode()), discount);
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
            List<PromotionSumaryRes> productPromotions,
            List<PromotionSumaryRes> categoryPromotions,
            List<PromotionSumaryRes> globalPromotions
    ) {
        // (VD: product thuộc 1 category mà cả product lẫn category đều được map với cùng promotion)
        Map<Integer, PromotionSumaryRes> dedupMap = new LinkedHashMap<>(
                productPromotions.size() + categoryPromotions.size() + globalPromotions.size()
        );
        productPromotions.forEach(p -> dedupMap.put(p.promotionId(), p));
        categoryPromotions.forEach(p -> dedupMap.put(p.promotionId(), p));
        globalPromotions.forEach(p -> dedupMap.put(p.promotionId(), p));
        List<PromotionSumaryRes> allPromotions = new ArrayList<>(dedupMap.values());

        List<PromotionSumaryRes> applicablePromotions = allPromotions.stream()
                .filter(promo -> promo.isApplicable(price, now))
                .toList();

        if (applicablePromotions.isEmpty()) {
            return DiscountInfoDto.noDiscount(price);
        }

        // Tìm promotion cho discount cao nhất
        PromotionSumaryRes bestPromotion = applicablePromotions.stream()
                .max(Comparator.comparing(p -> p.calculateDiscount(price)))
                .orElseThrow();

        BigDecimal discount = bestPromotion.calculateDiscount(price);
        BigDecimal finalPrice = price.subtract(discount);

        log.debug("Best promotion: type={}, value={}, discount={}, finalPrice={}",
                bestPromotion.type(), bestPromotion.value(), discount, finalPrice);

        return new DiscountInfoDto(price, finalPrice, bestPromotion.type().getValue(), bestPromotion.value());
    }
}
