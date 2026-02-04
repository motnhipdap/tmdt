package com.dev.dungcony.modules.promotions.reporitories;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import com.dev.dungcony.modules.promotions.entities.PromotionCategory;
import com.dev.dungcony.modules.promotions.entities.PromotionCategoryId;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface PromotionCategoryRepository extends JpaRepository<PromotionCategory, PromotionCategoryId> {


    @Query("""
            select new com.dev.dungcony.modules.promotions.dtos.res.PromotionDto(
                        pp.promotion.id,
                        pp.promotion.type,
                        pp.promotion.value,
                        pp.promotion.minPriceApply,
                        pp.promotion.startAt,
                        pp.promotion.endAt
                        )
            from PromotionCategory pp
            where pp.categoryId = :categoryId
                 and pp.promotion.status = :status
                 and pp.promotion.endAt > :now
               order by pp.promotion.priority desc
            """)
    List<PromotionDto> findByCategoryId(
            @Param("categoryId") Integer categoryId,
            @Param("now") Instant now,
            @Param("status") PromotionStatus status
    );

    @Query("""
            select exists (
            select 1
            from PromotionCategory pp
            where pp.categoryId = :categoryId
                 and pp.promotion.status = :status
                 and pp.promotion.endAt > :now
                           )
            """)
    boolean existsActivePromotion(
            @Param("categoryId") Integer categoryId,
            @Param("now") Instant now,
            @Param("status") PromotionStatus status
    );
}
