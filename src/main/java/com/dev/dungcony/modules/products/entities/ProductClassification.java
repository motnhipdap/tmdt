package com.dev.dungcony.modules.products.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_product_classifications", schema = "db1")
public class ProductClassification {
    @EmbeddedId
    private ProductClassificationId id;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @MapsId("classificationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "classification_id", nullable = false)
    private Classification classification;
}