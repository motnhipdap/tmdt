package com.dev.dungcony.modules.promotions.repositories;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionSummaryRes;
import com.dev.dungcony.modules.promotions.entities.Promotion;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

// PromotionRepository.java
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    /**
     * Lấy danh sách promotion (không bao gồm DELETED) cho admin.
     */
    @Query("""
                SELECT new com.dev.dungcony.modules.promotions.dtos.res.PromotionSummaryRes(
                    p.code,
                    p.type,
                    p.value,
                    p.minPriceApply,
                    p.startAt,
                    p.endAt
                )
                FROM Promotion p
                WHERE p.status <> com.dev.dungcony.modules.promotions.enums.PromotionStatus.DELETED
            """)
    Page<PromotionSummaryRes> getAll(Pageable pageable);

    @Query("""
            SELECT new com.dev.dungcony.modules.promotions.dtos.res.PromotionSummaryRes(
                p.code,
                p.type,
                p.value,
                p.minPriceApply,
                p.startAt,
                p.endAt
            )
            FROM Promotion p
            WHERE p.scope = 'GLOBAL'
                AND p.status = :status
                AND p.endAt > :now
            ORDER BY p.priority DESC
            """)
    List<PromotionSummaryRes> findGlobalPromotions(
            @Param("now") Instant now,
            @Param("status") PromotionStatus status);

    Optional<Promotion> findByCode(String code);

    boolean existsByCode(String code);

    /**
     * Bulk update: đánh dấu ENDED cho tất cả promotion ACTIVE đã hết hạn.
     * Tránh load entity rồi save lại từng cái.
     */
    @Modifying
    @Query("""
            UPDATE Promotion p
            SET p.status = com.dev.dungcony.modules.promotions.enums.PromotionStatus.ENDED
            WHERE p.status = :activeStatus
                AND p.endAt < :now
            """)
    int bulkExpirePromotions(
            @Param("activeStatus") PromotionStatus activeStatus,
            @Param("now") Instant now);

    /**
     * Bulk update: kích hoạt các promotion SCHEDULED đã đến giờ start và chưa hết
     * hạn.
     */
    @Modifying
    @Query("""
            UPDATE Promotion p
            SET p.status = com.dev.dungcony.modules.promotions.enums.PromotionStatus.ACTIVE
            WHERE p.status = :scheduledStatus
                AND p.startAt <= :now
                AND p.endAt > :now
            """)
    int bulkActivatePromotions(
            @Param("scheduledStatus") PromotionStatus scheduledStatus,
            @Param("now") Instant now);

    /**
     * Bulk update: đánh dấu ENDED cho các promotion SCHEDULED đã quá hạn luôn
     * (endAt < now).
     */
    @Modifying
    @Query("""
            UPDATE Promotion p
            SET p.status = com.dev.dungcony.modules.promotions.enums.PromotionStatus.ENDED
            WHERE p.status = :scheduledStatus
                AND p.endAt <= :now
            """)
    int bulkExpireScheduledPromotions(
            @Param("scheduledStatus") PromotionStatus scheduledStatus,
            @Param("now") Instant now);
}