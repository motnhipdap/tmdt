package com.dev.dungcony.modules.promotions.reporitories;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionDto;
import com.dev.dungcony.modules.promotions.entities.Promotion;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

// PromotionRepository.java
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    // Tìm các khuyến mãi đang hoạt động nhưng đã hết hạn
    List<Promotion> findByStatusAndEndAtBefore(PromotionStatus status, Instant now);

    // Tìm các khuyến mãi sắp diễn ra và đã đến giờ bắt đầu
    List<Promotion> findByStatusAndStartAtBefore(PromotionStatus status, Instant now);


    @Query("""
                SELECT new com.dev.dungcony.modules.promotions.dtos.res.PromotionDto(
                    p.id,
                    p.type,
                    p.value,
                    p.startAt,
                    p.endAt
                )
                FROM Promotion p
            """)
    Page<PromotionDto> getAll(
            Pageable pageable
    );
}