package com.dev.dungcony.modules.products.entities;

import com.dev.dungcony.modules.products.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_products", schema = "db1")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 255)
    @Column(name = "`desc`")
    private String description;

    @ColumnDefault("0")
    @Column(name = "quantity")
    private int quantity;

    @ColumnDefault("0")
    @Column(name = "quantity_sold")
    private int quantitySold;

    @ColumnDefault("0")
    @Column(name = "price")
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;
    
    @Column(name = "rated")
    private Float rated;

    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "create_at")
    private Instant createAt;

    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "update_at")
    private Instant updateAt;


    @OneToMany(
            mappedBy = "product",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductImg> images = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;
}