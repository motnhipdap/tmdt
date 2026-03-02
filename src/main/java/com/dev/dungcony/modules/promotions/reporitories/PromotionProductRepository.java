package com.dev.dungcony.modules.promotions.reporitories;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionSumaryRes;
import com.dev.dungcony.modules.promotions.entities.PromotionProduct;
import com.dev.dungcony.modules.promotions.entities.PromotionProductId;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface PromotionProductRepository extends JpaRepository<PromotionProduct, PromotionProductId> {

    @Query("""
            SELECT new com.dev.dungcony.modules.promotions.dtos.res.PromotionSumaryRes(
                             pp.promotion.id,
                             pp.promotion.type,
                             pp.promotion.value,
                             pp.promotion.minPriceApply,
                             pp.promotion.startAt,
                             pp.promotion.endAt
                         )
            FROM PromotionProduct pp
            WHERE pp.productId = :productId
                 AND pp.promotion.status = :status
                 AND pp.promotion.endAt > :now
            ORDER BY pp.promotion.priority DESC
            """)
    List<PromotionSumaryRes> findByProductId(
            @Param("productId") Integer productId,
            @Param("now") Instant now,
            @Param("status") PromotionStatus status
    );

    /**
     * Batch query: lấy tất cả promotions đang active cho danh sách productIds.
     * Tránh N+1 khi hiển thị danh sách sản phẩm.
     */
    @Query("""
            SELECT pp.productId,
                   new com.dev.dungcony.modules.promotions.dtos.res.PromotionSumaryRes(
                       pp.promotion.id,
                       pp.promotion.type,
                       pp.promotion.value,
                       pp.promotion.minPriceApply,
                       pp.promotion.startAt,
                       pp.promotion.endAt
                   )
            FROM PromotionProduct pp
            WHERE pp.productId IN :productIds
                 AND pp.promotion.status = :status
                 AND pp.promotion.endAt > :now
            ORDER BY pp.promotion.priority DESC
            """)
    List<Object[]> findByProductIds(
            @Param("productIds") List<Integer> productIds,
            @Param("now") Instant now,
            @Param("status") PromotionStatus status
    );

    /**
     * Kiểm tra product có tồn tại không trước khi tạo promotion-product mapping.
     */
    @Query("""
            SELECT pp.productId
            FROM PromotionProduct pp
            WHERE pp.id.promotionId = :promotionId
            """)
    List<Integer> findProductIdsByPromotionId(@Param("promotionId") Integer promotionId);

    void deleteAllByIdPromotionId(Integer promotionId);

    boolean existsByProductId(Integer productId);
}
