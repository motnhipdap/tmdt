package com.dev.dungcony.modules.products.entities;

import com.dev.dungcony.modules.products.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tbl_products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 10)
    @NotNull
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @ColumnDefault("0")
    @Column(name = "quantity")
    private int quantity;

    @ColumnDefault("0")
    @Column(name = "quantity_sold")
    private int quantitySold;

    @ColumnDefault("0")
    @Column(name = "price")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;

    @Column(name = "rated")
    private Float rated;

    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "updated_at")
    private Instant updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Size(max = 255)
    @Column(name = "img")
    private String img;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updateAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = Instant.now();
    }
}