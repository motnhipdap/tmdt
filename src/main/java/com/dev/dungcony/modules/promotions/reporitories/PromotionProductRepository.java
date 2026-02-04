package com.dev.dungcony.modules.promotions.reporitories;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import com.dev.dungcony.modules.promotions.entities.PromotionProduct;
import com.dev.dungcony.modules.promotions.entities.PromotionProductId;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface PromotionProductRepository extends JpaRepository<PromotionProduct, PromotionProductId> {

    @Query("""
            select new com.dev.dungcony.modules.promotions.dtos.res.PromotionDto(
                             pp.promotion.id,
                             pp.promotion.type,
                             pp.promotion.value,
                             pp.promotion.minPriceApply,
                             pp.promotion.startAt,
                             pp.promotion.endAt
                         )
            from PromotionProduct pp
            where pp.productId = :productId
                 and pp.promotion.status = :status
                 and pp.promotion.endAt > :now
               order by pp.promotion.priority desc
            """)
    List<PromotionDto> findByProductId(
            @Param("productId") Integer productId,
            @Param("now") Instant now,
            @Param("status") PromotionStatus status
    );
}
