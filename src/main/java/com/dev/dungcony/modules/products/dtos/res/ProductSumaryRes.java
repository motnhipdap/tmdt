package com.dev.dungcony.modules.products.dtos.res;

import java.math.BigDecimal;

import com.dev.dungcony.modules.products.dtos.DiscountInfoDto;

/**
 * Lightweight projection for product listing. Matches JPQL projections in
 * repository.
 */
public record ProductSumaryRes(
                Integer id,
                String name,
                BigDecimal price,
                Float rated,
                String imgUrl,
                Integer categoryId) {

        public ProductSumaryRes withDiscount(DiscountInfoDto discount) {
                if (discount == null) {
                        return this;
                }
                return new ProductSumaryRes(
                                id,
                                name,
                                discount.finalPrice(),
                                rated,
                                imgUrl,
                                categoryId);
        }
}
