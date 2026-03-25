package com.dev.dungcony.modules.promotion.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dev.dungcony.modules.promotion.dtos.req.PromoAddReq;
import com.dev.dungcony.modules.promotion.dtos.req.PromoUpdateReq;
import com.dev.dungcony.modules.promotion.dtos.res.PromotionDetailRes;
import com.dev.dungcony.modules.promotion.dtos.res.PromotionSummaryRes;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PromotionService {
    String addNew(PromoAddReq req);

    void update(PromoUpdateReq req);

    void delete(String code);

    void softDelete(String code);

    Page<PromotionSummaryRes> getAll(Pageable pageable);

    Optional<PromotionDetailRes> getByCode(String code);

    List<PromotionSummaryRes> getGlobalPromotions(Instant now);
}
