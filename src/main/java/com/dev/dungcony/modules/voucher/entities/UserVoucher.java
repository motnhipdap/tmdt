package com.dev.dungcony.modules.voucher.entities;

import com.dev.dungcony.commons.entities.BaseEntity;
import com.dev.dungcony.modules.order.entities.OrderItemId;
import com.dev.dungcony.modules.voucher.enums.UserVoucherStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
        name = "tbl_user_vouchers",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_voucher", columnNames = {"user_id", "voucher_id"}))
public class UserVoucher extends BaseEntity {

    @EmbeddedId
    private UserVoucherId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("voucher_id")
    @JoinColumn(name = "voucher_id", nullable = false)
    private Voucher voucher;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserVoucherStatus status = UserVoucherStatus.AVAILABLE;

    @Column(name = "used_at")
    private Instant usedAt;

    @Column(name = "used_order_code", length = 20)
    private String usedOrderCode;

    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;
}
