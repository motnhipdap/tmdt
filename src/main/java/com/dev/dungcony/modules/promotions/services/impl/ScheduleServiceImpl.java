package com.dev.dungcony.modules.promotions.services.impl;

import com.dev.dungcony.modules.promotions.entities.Promotion;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import com.dev.dungcony.modules.promotions.reporitories.PromotionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduleServiceImpl {

    private final PromotionRepository promotionRepository;

    /**
     * Tác vụ này chạy mỗi phút để kiểm tra và cập nhật các khuyến mãi đã hết hạn.
     * cron = "0 * * * * ?" có nghĩa là chạy vào giây thứ 0 của mỗi phút.
     */
    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void updateExpiredPromotions() {
        log.info("Running job to update expired promotions...");
        Instant now = Instant.now();

        List<Promotion> activePromotions = promotionRepository.findByStatusAndEndAtBefore(PromotionStatus.ACTIVE, now);

        if (!activePromotions.isEmpty()) {
            log.info("Found {} promotions to expire.", activePromotions.size());
            for (Promotion promotion : activePromotions) {
                promotion.setStatus(PromotionStatus.ENDED);
            }
            promotionRepository.saveAll(activePromotions);
        }
    }

    /**
     * Tác vụ này chạy mỗi phút để kích hoạt các khuyến mãi đã đến giờ.
     */
    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void activateScheduledPromotions() {
        log.info("Running job to activate scheduled promotions...");
        Instant now = Instant.now();

        List<Promotion> scheduledPromotions = promotionRepository.findByStatusAndStartAtBefore(PromotionStatus.SCHEDULED, now);

        if (!scheduledPromotions.isEmpty()) {
            log.info("Found {} promotions to activate.", scheduledPromotions.size());
            for (Promotion promotion : scheduledPromotions) {
                // Kiểm tra lại để chắc chắn nó chưa hết hạn (trường hợp thời gian KM rất ngắn)
                if (promotion.getEndAt().isAfter(now)) {
                    promotion.setStatus(PromotionStatus.ACTIVE);
                } else {
                    promotion.setStatus(PromotionStatus.ENDED);
                }
            }
            promotionRepository.saveAll(scheduledPromotions);
        }
    }
}
