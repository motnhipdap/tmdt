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
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tbl_categories", schema = "db1",
        indexes = {
                @Index(name = "idx_category_name", columnList = "name")
        }
)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 255)
    @Column(name = "img_url")
    private String imgUrl;

    @Size(max = 50)
    @Column(name = "`desc`", length = 50)
    private String desc;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private CategoryStatus status = CategoryStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "create_at")
    private Instant createAt;

    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "update_at")
    private Instant updateAt;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Category> tblCategories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<Product> tblProducts = new LinkedHashSet<>();

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}