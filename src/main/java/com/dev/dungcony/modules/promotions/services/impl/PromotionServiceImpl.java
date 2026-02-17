package com.dev.dungcony.modules.promotions.services.impl;

import com.dev.dungcony.modules.products.dtos.DiscountInfo;
import com.dev.dungcony.modules.products.services.interfaces.PromotionCalculator;
import com.dev.dungcony.modules.promotions.dtos.req.PromoAddReq;
import com.dev.dungcony.modules.promotions.dtos.req.PromoUpdateReq;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import com.dev.dungcony.modules.promotions.entities.Promotion;
import com.dev.dungcony.modules.promotions.enums.PromotionScope;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import com.dev.dungcony.modules.promotions.enums.PromotionType;
import com.dev.dungcony.modules.promotions.exceptions.InvalidPromotionException;
import com.dev.dungcony.modules.promotions.exceptions.PromotionNotFoundException;
import com.dev.dungcony.modules.promotions.reporitories.PromotionRepository;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionCategoryService;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionProductService;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service("v1-promotion")
public class PromotionServiceImpl implements PromotionService, PromotionCalculator {

    private final PromotionRepository promotionRepository;
    private final PromotionProductService promotionProductService;
    private final PromotionCategoryService promotionCategoryService;

    @Transactional
    @Override
    public int addNew(PromoAddReq req) {
        log.info("adding new promotion: {}", req);

        // Validate business rules
        if (req.endAt().isBefore(req.startAt())) {
            throw new InvalidPromotionException("End date must be after start date");
        }

        if (req.scope() == PromotionScope.PRODUCT && (req.productIds() == null || req.productIds().isEmpty())) {
            throw new InvalidPromotionException("Product IDs are required for PRODUCT scope");
        }

        if (req.scope() == PromotionScope.CATEGORY && (req.categoryIds() == null || req.categoryIds().isEmpty())) {
            throw new InvalidPromotionException("Category IDs are required for CATEGORY scope");
        }

        Promotion promotion = new Promotion();
        promotion.setType(req.type());
        promotion.setValue(req.value());
        promotion.setScope(req.scope());
        promotion.setStartAt(req.startAt());
        promotion.setEndAt(req.endAt());
        promotion.setPriority(req.priority());
        promotion.setMinPriceApply(req.priceRequire());

        promotion = promotionRepository.save(promotion);

        if (req.scope() == PromotionScope.PRODUCT)
            promotionProductService.addListPromotionProduct(promotion, req.productIds());

        if (req.scope() == PromotionScope.CATEGORY)
            promotionCategoryService.addListPromotionCategory(promotion, req.categoryIds());

        return promotion.getId();
    }

    @Override
    public void delete(Integer promotionId) {
        log.info("Attempting to soft-delete promotion with id: {}", promotionId);
        promotionRepository.findById(promotionId).ifPresent(promotion -> {
            promotion.setStatus(PromotionStatus.DELETED);
            promotionRepository.save(promotion);
            log.info("Successfully soft-deleted promotion with id: {}", promotionId);
        });
    }

    // trả về page toàn bộ pgg dành cho admin
    @Override
    public Page<PromotionDto> getAll(Pageable pageable) {
        return promotionRepository.getAll(pageable);
    }

    @Override
    public void remove(Integer promotionId) {
        promotionRepository.deleteById(promotionId);
    }

    @Transactional
    @Override
    public void update(PromoUpdateReq req) {
        log.info("Updating promotion with id: {}", req.id());

        Promotion promotion = promotionRepository.findById(req.id())
                .orElseThrow(() -> new PromotionNotFoundException(req.id()));

        // Validate dates if both are provided
        if (req.startAt() != null && req.endAt() != null && req.endAt().isBefore(req.startAt())) {
            throw new InvalidPromotionException("End date must be after start date");
        }

        // Update only provided fields
        if (req.type() != null) {
            promotion.setType(req.type());
        }
        if (req.value() != null) {
            if (promotion.getType() == PromotionType.PERCENT && (req.value() < 0 || req.value() > 100)) {
                throw new InvalidPromotionException("Percent value must be between 0 and 100");
            }
            promotion.setValue(req.value());
        }
        if (req.startAt() != null) {
            promotion.setStartAt(req.startAt());
        }
        if (req.endAt() != null) {
            promotion.setEndAt(req.endAt());
        }
        if (req.priority() != null) {
            promotion.setPriority(req.priority());
        }
        if (req.priceRequire() != null) {
            promotion.setMinPriceApply(req.priceRequire());
        }
        if (req.status() != null) {
            promotion.setStatus(req.status());
        }

        promotionRepository.save(promotion);
        log.info("Successfully updated promotion with id: {}", req.id());
    }

    @Override
    public Optional<PromotionDto> getById(Integer id) {
        return promotionRepository.findById(id)
                .map(p -> new PromotionDto(
                        p.getId(),
                        p.getType(),
                        p.getValue(),
                        p.getMinPriceApply(),
                        p.getStartAt(),
                        p.getEndAt()));
    }

    @Override
    public DiscountInfo calculateFinalPrice(int productId, int categoryId, int price) {
        log.info("Calculating final price for productId: {}, categoryId: {}, price: {}", productId, categoryId, price);

        // Get all applicable promotions
        List<PromotionDto> productPromotions = promotionProductService.getPromotionByProduct(productId);
        List<PromotionDto> categoryPromotions = promotionCategoryService.getPromotionByCategory(categoryId);
        List<PromotionDto> globalPromotions = promotionRepository.findGlobalPromotions(
                Instant.now(),
                PromotionStatus.ACTIVE
        );

        // Merge all promotions into one list
        List<PromotionDto> allPromotions = new ArrayList<>();
        allPromotions.addAll(productPromotions);
        allPromotions.addAll(categoryPromotions);
        allPromotions.addAll(globalPromotions);

        // Filter promotions that are applicable based on minimum price
        List<PromotionDto> applicablePromotions = allPromotions.stream()
                .filter(promo -> promo.isApplicable(price))
                .toList();

        if (applicablePromotions.isEmpty()) {
            log.info("No applicable promotions found");
            return new DiscountInfo((double) price, (double) price, "NONE", 0);
        }

        // Find the best promotion (one that gives the highest discount)
        PromotionDto bestPromotion = applicablePromotions.stream()
                .max((p1, p2) -> {
                    int discount1 = calculateDiscount(p1, price);
                    int discount2 = calculateDiscount(p2, price);
                    return Integer.compare(discount1, discount2);
                })
                .orElseThrow();

        // Calculate final price
        int discount = calculateDiscount(bestPromotion, price);
        int finalPrice = price - discount;

        log.info("Best promotion found: type={}, value={}, discount={}, finalPrice={}",
                bestPromotion.type(), bestPromotion.value(), discount, finalPrice);

        return new DiscountInfo(
                (double) price,
                (double) finalPrice,
                bestPromotion.type().getValue(),
                bestPromotion.value());
    }

    private int calculateDiscount(PromotionDto promotion, int price) {
        return switch (promotion.type()) {
            case PERCENT -> price * promotion.value() / 100;
            case FIXED -> Math.min(promotion.value(), price); // Don't discount more than the price
        };
    }
}
