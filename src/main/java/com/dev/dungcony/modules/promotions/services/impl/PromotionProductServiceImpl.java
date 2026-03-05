package com.dev.dungcony.modules.promotions.services.impl;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionSumaryRes;
import com.dev.dungcony.modules.promotions.entities.Promotion;
import com.dev.dungcony.modules.promotions.entities.PromotionProduct;
import com.dev.dungcony.modules.promotions.entities.PromotionProductId;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import com.dev.dungcony.modules.promotions.reporitories.PromotionProductRepository;
import com.dev.dungcony.modules.promotions.services.interfaces.GetIdByCode;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionProductService;
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
public class PromotionProductServiceImpl implements PromotionProductService {
    private final PromotionProductRepository promotionProductRepository;
    private final GetIdByCode getIdByCode;

    @Override
    public List<PromotionSumaryRes> getPromotionByProduct(String code) {
        return promotionProductRepository.findByProductId(getIdByCode.getByProductCode(code), Instant.now(), PromotionStatus.ACTIVE);
    }

    @Override
    public Map<Integer, List<PromotionSumaryRes>> getPromotionsByProducts(List<String> productCodes) {
        if (productCodes == null || productCodes.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Object[]> rows = promotionProductRepository
                .findByProductIds(getIdByCode.getByCategoryCodes(productCodes), Instant.now(), PromotionStatus.ACTIVE);

        return rows.stream()
                .collect(Collectors.groupingBy(
                        row -> (Integer) row[0],
                        Collectors.mapping(row -> (PromotionSumaryRes) row[1], Collectors.toList())
                ));
    }

    @Transactional
    @Override
    public void addListPromotionProduct(Promotion pro, List<String> productCodes) {
        log.info("Adding {} promotion-product mappings for promotionId={}", productCodes.size(), pro.getId());

        List<PromotionProduct> mappings = productCodes.stream()
                .map(productCode -> {
                    PromotionProduct pp = new PromotionProduct();
                    pp.setPromotion(pro);
                    pp.setId(new PromotionProductId(getIdByCode.getByProductCode(productCode), pro.getId()));
                    return pp;
                })
                .toList();

        promotionProductRepository.saveAll(mappings);
    }
}
