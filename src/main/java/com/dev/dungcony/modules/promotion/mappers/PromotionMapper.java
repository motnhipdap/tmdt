package com.dev.dungcony.modules.promotion.mappers;

import org.springframework.stereotype.Component;

import com.dev.dungcony.modules.promotion.dtos.res.PromotionDetailRes;
import com.dev.dungcony.modules.promotion.dtos.res.PromotionSummaryRes;
import com.dev.dungcony.modules.promotion.entities.Promotion;

/**
 * Mapper chuyển đổi giữa Promotion entity và các DTO response.
 * Tách riêng để tuân thủ SRP và tránh rò rỉ internal ID ra client.
 */
@Component
public class PromotionMapper {

    /**
     * Entity → PromotionDetailRes (dùng cho endpoint get single promotion).
     * Không bao gồm internal id — client dùng code làm định danh.
     */
    public PromotionDetailRes toDetailRes(Promotion p) {
        return new PromotionDetailRes(
                p.getValue(),
                p.getScope(),
                p.getStartAt(),
                p.getEndAt(),
                p.getPriority(),
                p.getStatus());
    }

    /**
     * Entity → PromotionSummaryRes (dùng cho danh sách / batch query).
     * Không bao gồm internal id — code là business key duy nhất.
     */
    public PromotionSummaryRes toSummaryRes(Promotion p) {
        return new PromotionSummaryRes(
                p.getValue(),
                p.getStartAt(),
                p.getEndAt());
    }
}
