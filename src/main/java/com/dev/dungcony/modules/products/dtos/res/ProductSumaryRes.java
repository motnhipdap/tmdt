package com.dev.dungcony.modules.products.dtos.res;

import java.math.BigDecimal;

import com.dev.dungcony.modules.products.dtos.DiscountInfoDto;

/**
 * Lightweight projection for product listing. Matches JPQL projections in
 * repository.
 */
public record ProductSumaryRes(
        String code,
        String name,
        BigDecimal price,
        Float rated,
        String imgUrl,
        Integer categoryCode) {

    public ProductSumaryRes withDiscount(DiscountInfoDto discount) {
        if (discount == null) {
            return this;
        }
        return new ProductSumaryRes(
                code,
                name,
                discount.finalPrice(),
                rated,
                imgUrl,
                categoryCode);
    }
}
