package com.dev.dungcony.modules.promotions.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Table(name = "tbl_promotion_category")
public class PromotionCategory extends EntityBase {
    @EmbeddedId
    private PromotionCategoryId id;

    @Column(name = "category_id", nullable = false, insertable = false, updatable = false)
    private Integer categoryId;

    @NotNull
    @MapsId("promotionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;
}