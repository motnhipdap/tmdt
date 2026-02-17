package com.dev.dungcony.modules.promotions.services.interfaces;

import com.dev.dungcony.modules.promotions.dtos.req.PromoAddReq;
import com.dev.dungcony.modules.promotions.dtos.req.PromoUpdateReq;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PromotionService {
    int addNew(PromoAddReq req);

    void update(PromoUpdateReq req);

    void remove(Integer promotionId);

    void delete(Integer promotionId);

    Page<PromotionDto> getAll(Pageable pageable);

    Optional<PromotionDto> getById(Integer id);
}
