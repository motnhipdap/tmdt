package com.dev.dungcony.modules.promotions.services.interfaces;

import com.dev.dungcony.modules.promotions.dtos.req.PromoAddReq;
import com.dev.dungcony.modules.promotions.dtos.req.PromoUpdateReq;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionDetailRes;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionSumaryRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PromotionService {
    int addNew(PromoAddReq req);

    void update(PromoUpdateReq req);

    void delete(Integer promotionId);

    void softDelete(Integer promotionId);

    Page<PromotionSumaryRes> getAll(Pageable pageable);

    Optional<PromotionDetailRes> getById(Integer id);

    /**
     * Lấy danh sách global promotions đang active tại thời điểm now.
     */
    List<PromotionSumaryRes> getGlobalPromotions(Instant now);
}
