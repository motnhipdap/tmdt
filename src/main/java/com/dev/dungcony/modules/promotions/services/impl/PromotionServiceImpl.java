package com.dev.dungcony.modules.promotions.services.impl;

import com.dev.dungcony.modules.products.repositories.CategoryRepository;
import com.dev.dungcony.modules.products.repositories.ProductRepository;
import com.dev.dungcony.modules.products.enums.ProductStatus;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service quản lý CRUD promotion.
 * Logic tính toán giá đã được tách sang {@link PromotionCalculatorImpl}.
 */
@Slf4j
@RequiredArgsConstructor
@Service("v1-promotion")
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionProductService promotionProductService;
    private final PromotionCategoryService promotionCategoryService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public int addNew(PromoAddReq req) {
        log.info("Adding new promotion: {}", req);

        validateAddRequest(req);

        // Xác định status ban đầu dựa trên thời gian
        PromotionStatus initialStatus = determineInitialStatus(req.startAt(), req.endAt());

        Promotion promotion = new Promotion();
        promotion.setType(req.type());
        promotion.setValue(req.value());
        promotion.setScope(req.scope());
        promotion.setStartAt(req.startAt());
        promotion.setEndAt(req.endAt());
        promotion.setPriority(req.priority());
        promotion.setMinPriceApply(req.priceRequire() != null
                ? BigDecimal.valueOf(req.priceRequire())
                : BigDecimal.ZERO);
        promotion.setStatus(initialStatus);

        promotion = promotionRepository.save(promotion);

        // Tạo mapping với product/category
        if (req.scope() == PromotionScope.PRODUCT) {
            promotionProductService.addListPromotionProduct(promotion, req.productIds());
        }
        if (req.scope() == PromotionScope.CATEGORY) {
            promotionCategoryService.addListPromotionCategory(promotion, req.categoryIds());
        }

        return promotion.getId();
    }

    @Override
    public void delete(Integer promotionId) {
        log.info("deleting promotion id={}", promotionId);
        promotionRepository.deleteById(promotionId);
    }

    @Override
    public void softDelete(Integer promotionId) {
        log.info("Soft-deleting promotion id={}", promotionId);
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new PromotionNotFoundException(promotionId));

        promotion.setStatus(PromotionStatus.DELETED);
        promotionRepository.save(promotion);
    }

    @Override
    public Page<PromotionDto> getAll(Pageable pageable) {
        return promotionRepository.getAll(pageable);
    }

    @Override
    public void remove(Integer promotionId) {
        if (!promotionRepository.existsById(promotionId)) {
            throw new PromotionNotFoundException(promotionId);
        }
        promotionRepository.deleteById(promotionId);
    }

    @Transactional
    @Override
    public void update(PromoUpdateReq req) {
        log.info("Updating promotion id={}", req.id());

        Promotion promotion = promotionRepository.findById(req.id())
                .orElseThrow(() -> new PromotionNotFoundException(req.id()));

        // Không cho update promotion đã DELETED
        if (promotion.getStatus() == PromotionStatus.DELETED) {
            throw new InvalidPromotionException("Cannot update a deleted promotion");
        }

        // Xác định startAt và endAt cuối cùng để validate
        Instant effectiveStart = req.startAt() != null ? req.startAt() : promotion.getStartAt();
        Instant effectiveEnd = req.endAt() != null ? req.endAt() : promotion.getEndAt();

        if (effectiveEnd.isBefore(effectiveStart)) {
            throw new InvalidPromotionException("End date must be after start date");
        }

        // Xác định type cuối cùng để validate value
        PromotionType effectiveType = req.type() != null ? req.type() : promotion.getType();

        if (req.value() != null && effectiveType == PromotionType.PERCENT
                && (req.value() < 0 || req.value() > 100)) {
            throw new InvalidPromotionException("Percent value must be between 0 and 100");
        }

        // Update only provided fields
        if (req.type() != null) promotion.setType(req.type());
        if (req.value() != null) promotion.setValue(req.value());
        if (req.startAt() != null) promotion.setStartAt(req.startAt());
        if (req.endAt() != null) promotion.setEndAt(req.endAt());
        if (req.priority() != null) promotion.setPriority(req.priority());
        if (req.priceRequire() != null) promotion.setMinPriceApply(BigDecimal.valueOf(req.priceRequire()));
        if (req.status() != null) promotion.setStatus(req.status());

        promotionRepository.save(promotion);
        log.info("Successfully updated promotion id={}", req.id());
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
    public List<PromotionDto> getGlobalPromotions(Instant now) {
        return promotionRepository.findGlobalPromotions(now, PromotionStatus.ACTIVE);
    }

    // ============ PRIVATE HELPERS ============

    private void validateAddRequest(PromoAddReq req) {
        if (req.endAt().isBefore(req.startAt())) {
            throw new InvalidPromotionException("End date must be after start date");
        }

        if (req.scope() == PromotionScope.PRODUCT) {
            if (req.productIds() == null || req.productIds().isEmpty()) {
                throw new InvalidPromotionException("Product IDs are required for PRODUCT scope");
            }
            // Validate tất cả productIds tồn tại và đang ACTIVE
            long existCount = productRepository.countByIdInAndStatus(req.productIds(), ProductStatus.ACTIVE);
            if (existCount != req.productIds().size()) {
                throw new InvalidPromotionException(
                        "Some product IDs are invalid or inactive. Expected " + req.productIds().size()
                                + " but found " + existCount
                );
            }
        }

        if (req.scope() == PromotionScope.CATEGORY) {
            if (req.categoryIds() == null || req.categoryIds().isEmpty()) {
                throw new InvalidPromotionException("Category IDs are required for CATEGORY scope");
            }
            // Validate tất cả categoryIds tồn tại
            long existCount = categoryRepository.countByIdIn(req.categoryIds());
            if (existCount != req.categoryIds().size()) {
                throw new InvalidPromotionException(
                        "Some category IDs are invalid. Expected " + req.categoryIds().size()
                                + " but found " + existCount
                );
            }
        }
    }

    /**
     * Xác định status ban đầu: nếu startAt đã qua -> ACTIVE, nếu chưa -> SCHEDULED.
     */
    private PromotionStatus determineInitialStatus(Instant startAt, Instant endAt) {
        Instant now = Instant.now();
        if (endAt.isBefore(now)) {
            throw new InvalidPromotionException("Cannot create promotion that has already ended");
        }
        return startAt.isBefore(now) ? PromotionStatus.ACTIVE : PromotionStatus.SCHEDULED;
    }
}
