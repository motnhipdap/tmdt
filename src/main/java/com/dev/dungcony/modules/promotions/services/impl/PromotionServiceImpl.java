package com.dev.dungcony.modules.promotions.services.impl;

import com.dev.dungcony.modules.promotions.dtos.req.PromoAddReq;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import com.dev.dungcony.modules.promotions.entities.Promotion;
import com.dev.dungcony.modules.promotions.enums.PromotionScope;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
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

@Slf4j
@RequiredArgsConstructor
@Service("v1-promotion")
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionProductService promotionProductService;
    private final PromotionCategoryService promotionCategoryService;

    @Transactional
    @Override
    public int addNew(PromoAddReq req) {
        log.info("adding new promotion: {}", req);

        Promotion promotion = new Promotion();
        promotion.setType(req.type());
        promotion.setValue(req.value());
        promotion.setScope(req.scope());
        promotion.setStartAt(req.startAt());
        promotion.setEndAt(req.endAt());
        promotion.setPriority(req.priority());
        promotion.setMinPriceApply(req.priceRequire());

        promotion = promotionRepository.save(promotion);

        if (req.scope() == PromotionScope.PRODUCT && req.productIds() != null)
            promotionProductService.addListPromotionProduct(promotion, req.productIds());

        if (req.scope() == PromotionScope.CATEGORY && req.categoryIds() != null)
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

    @Override
    public Page<PromotionDto> getAll(Pageable pageable) {
        return promotionRepository.getAll(pageable);
    }

    @Override
    public void remove(Integer promotionId) {
        promotionRepository.deleteById(promotionId);
    }
}
