package com.dev.dungcony.modules.promotions.entities;

import jakarta.persistence.*;

@Table(name = "tbl_promotion_product")
public class PromotionProduct extends EntityBase {
    @EmbeddedId
    private PromotionProductId id;

    @Column(name = "product_id", nullable = false, insertable = false, updatable = false)
    private Integer productId;

    @MapsId("promotionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;
}