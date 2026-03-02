package com.dev.dungcony.modules.promotions.entities;

import com.dev.dungcony.modules.promotions.enums.PromotionScope;
import com.dev.dungcony.modules.promotions.enums.PromotionStatus;
import com.dev.dungcony.modules.promotions.enums.PromotionType;
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
@Table(name = "tbl_promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int UNSIGNED not null")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private PromotionType type = PromotionType.PERCENT;

    @NotNull
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @NotNull
    @Column(name = "value", nullable = false)
    private Integer value;

    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "start_at")
    private Instant startAt;

    @Column(name = "end_at")
    private Instant endAt;

    @ColumnDefault("1")
    @Column(name = "priority")
    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private PromotionStatus status = PromotionStatus.SCHEDULED;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "min_price_apply", nullable = false)
    private BigDecimal minPriceApply;
    @Size(max = 20)

    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false, length = 20)
    private PromotionScope scope = PromotionScope.GLOBAL;
    @NotNull
    @ColumnDefault("0")
    @Column(name = "version", nullable = false)
    private Long version;
}