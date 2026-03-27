package com.dev.dungcony.modules.order.entities;

import java.math.BigDecimal;

import com.dev.dungcony.modules.product.entities.Product;
import com.dev.dungcony.modules.product.entities.Size;

import com.dev.dungcony.modules.product.enums.ProductSize;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "tbl_order_items")
public class OrderItem {

    @EmbeddedId
    private OrderItemId id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private BigDecimal totaPrice;


    //---fk---//
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;
}
