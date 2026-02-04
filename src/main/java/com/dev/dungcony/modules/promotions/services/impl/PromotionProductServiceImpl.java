package com.dev.dungcony.modules.promotions.services.impl;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import com.dev.dungcony.modules.promotions.entities.Promotion;
import com.dev.dungcony.modules.promotions.entities.PromotionProduct;
import com.dev.dungcony.modules.promotions.entities.PromotionProductId;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import com.dev.dungcony.modules.promotions.reporitories.PromotionProductRepository;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionProductServiceImpl implements PromotionProductService {
    private final PromotionProductRepository promotionProductRepository;

    @Override
    public List<PromotionDto> getPromotionByProduct(Integer productId) {
        return promotionProductRepository.findByProductId(productId, Instant.now(), PromotionStatus.ACTIVE);
    }

    @Transactional
    @Override
    public void addListPromotionProduct(Promotion pro, List<Integer> productIds) {

        log.info("adding list promotion product.....");

        List<PromotionProduct> mappings = productIds.stream()
                .map(productId -> {
                    PromotionProduct pp = new PromotionProduct();
                    pp.setPromotion(pro);
                    pp.setId(new PromotionProductId(productId, pro.getId()));
                    return pp;
                })
                .toList();

        promotionProductRepository.saveAll(mappings);
    }
}
