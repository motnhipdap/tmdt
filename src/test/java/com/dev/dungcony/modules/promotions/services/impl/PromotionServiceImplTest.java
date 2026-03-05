package com.dev.dungcony.modules.promotions.services.impl;

import com.dev.dungcony.modules.promotions.dtos.req.PromoAddReq;
import com.dev.dungcony.modules.promotions.dtos.req.PromoUpdateReq;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionSummaryRes;
import com.dev.dungcony.modules.promotions.entities.Promotion;
import com.dev.dungcony.modules.promotions.enums.PromotionScope;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import com.dev.dungcony.modules.promotions.enums.PromotionType;
import com.dev.dungcony.modules.promotions.exceptions.InvalidPromotionException;
import com.dev.dungcony.modules.promotions.exceptions.PromotionNotFoundException;
import com.dev.dungcony.modules.promotions.repositories.PromotionRepository;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionCategoryService;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromotionServiceImplTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PromotionProductService promotionProductService;

    @Mock
    private PromotionCategoryService promotionCategoryService;

    @InjectMocks
    private PromotionServiceImpl promotionService;

    private PromoAddReq validPromoAddReq;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        Instant now = Instant.now();
        Instant future = now.plusSeconds(3600);

        validPromoAddReq = new PromoAddReq(
                PromotionType.PERCENT,
                10,
                PromotionScope.GLOBAL,
                now,
                future,
                1,
                0,
                null,
                null);

        promotion = new Promotion();
        promotion.setId(1);
        promotion.setType(PromotionType.PERCENT);
        promotion.setValue(10);
        promotion.setScope(PromotionScope.GLOBAL);
        promotion.setStartAt(now);
        promotion.setEndAt(future);
        promotion.setPriority(1);
        promotion.setMinPriceApply(0);
        promotion.setStatus(PromotionStatus.SCHEDULED);
    }

    @Test
    void addNew_WithValidGlobalPromotion_ShouldSucceed() {
        // Given
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

        // When
        int result = promotionService.addNew(validPromoAddReq);

        // Then
        assertThat(result).isEqualTo(1);
        verify(promotionRepository).save(any(Promotion.class));
        verify(promotionProductService, never()).addListPromotionProduct(any(), any());
        verify(promotionCategoryService, never()).addListPromotionCategory(any(), any());
    }

    @Test
    void addNew_WithProductScopeAndProducts_ShouldSucceed() {
        // Given
        Instant now = Instant.now();
        Instant future = now.plusSeconds(3600);

        PromoAddReq productPromoReq = new PromoAddReq(
                PromotionType.PERCENT,
                10,
                PromotionScope.PRODUCT,
                now,
                future,
                1,
                0,
                List.of(1, 2, 3),
                null);

        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

        // When
        promotionService.addNew(productPromoReq);

        // Then
        verify(promotionRepository).save(any(Promotion.class));
        verify(promotionProductService).addListPromotionProduct(any(Promotion.class), eq(List.of(1, 2, 3)));
    }

    @Test
    void addNew_WithProductScopeButNoProducts_ShouldThrowException() {
        // Given
        Instant now = Instant.now();
        Instant future = now.plusSeconds(3600);

        PromoAddReq invalidReq = new PromoAddReq(
                PromotionType.PERCENT,
                10,
                PromotionScope.PRODUCT,
                now,
                future,
                1,
                0,
                null,
                null);

        // When/Then
        assertThatThrownBy(() -> promotionService.addNew(invalidReq))
                .isInstanceOf(InvalidPromotionException.class)
                .hasMessageContaining("Product IDs are required");
    }

    @Test
    void addNew_WithEndDateBeforeStartDate_ShouldThrowException() {
        // Given
        Instant now = Instant.now();
        Instant past = now.minusSeconds(3600);

        PromoAddReq invalidReq = new PromoAddReq(
                PromotionType.PERCENT,
                10,
                PromotionScope.GLOBAL,
                now,
                past,
                1,
                0,
                null,
                null);

        // When/Then
        assertThatThrownBy(() -> promotionService.addNew(invalidReq))
                .isInstanceOf(InvalidPromotionException.class)
                .hasMessageContaining("End date must be after start date");
    }

    @Test
    void addNew_WithInvalidPercentValue_ShouldThrowException() {
        // Given
        Instant now = Instant.now();
        Instant future = now.plusSeconds(3600);

        PromoAddReq invalidReq = new PromoAddReq(
                PromotionType.PERCENT,
                150,
                PromotionScope.GLOBAL,
                now,
                future,
                1,
                0,
                null,
                null);

        // When/Then
        assertThatThrownBy(() -> promotionService.addNew(invalidReq))
                .isInstanceOf(InvalidPromotionException.class)
                .hasMessageContaining("Percent value must be between 0 and 100");
    }

    @Test
    void update_WithValidData_ShouldSucceed() {
        // Given
        PromoUpdateReq updateReq = new PromoUpdateReq(
                1,
                PromotionType.FIXED,
                5000,
                null,
                null,
                2,
                1000,
                PromotionStatus.ACTIVE);

        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

        // When
        promotionService.update(updateReq);

        // Then
        verify(promotionRepository).findById(1);
        verify(promotionRepository).save(any(Promotion.class));
    }

    @Test
    void update_WithNonExistentId_ShouldThrowException() {
        // Given
        PromoUpdateReq updateReq = new PromoUpdateReq(
                999,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        when(promotionRepository.findById(999)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> promotionService.update(updateReq))
                .isInstanceOf(PromotionNotFoundException.class);
    }

    @Test
    void delete_WithExistingId_ShouldSoftDelete() {
        // Given
        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));
        when(promotionRepository.save(any(Promotion.class))).thenReturn(promotion);

        // When
        promotionService.delete(1);

        // Then
        verify(promotionRepository).findById(1);
        verify(promotionRepository).save(argThat(p -> p.getStatus() == PromotionStatus.DELETED));
    }

    @Test
    void getById_WithExistingId_ShouldReturnPromotionDto() {
        // Given
        when(promotionRepository.findById(1)).thenReturn(Optional.of(promotion));

        // When
        Optional<PromotionSummaryRes> result = promotionService.getById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().promotionId()).isEqualTo(1);
        assertThat(result.get().type()).isEqualTo(PromotionType.PERCENT);
        assertThat(result.get().value()).isEqualTo(10);
    }

    @Test
    void getById_WithNonExistentId_ShouldReturnEmpty() {
        // Given
        when(promotionRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<PromotionSummaryRes> result = promotionService.getById(999);

        // Then
        assertThat(result).isEmpty();
    }
}
