package com.dev.dungcony.modules.products.entities;

import com.dev.dungcony.modules.products.enums.CategoryStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tbl_categories", indexes = {
        @Index(name = "idx_category_name", columnList = "name"),
        @Index(name = "idx_category_path", columnList = "path")
})
public class Category {
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
    @Column(name = "category_code", nullable = false, length = 10)
    private String categoryCode;

    @Size(max = 255)
    @Column(name = "img_url")
    private String imgUrl;

    @Size(max = 50)
    @Column(name = "description", length = 50)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private CategoryStatus status = CategoryStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "is_leaf", nullable = false)
    private Boolean isLeaf;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "level", nullable = false)
    private Integer level = 0;

    @Size(max = 255)
    @Column(name = "path")
    private String path;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "created_at")
    private Instant createdAt;
    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}