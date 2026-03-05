package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.DiscountInfoDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PromotionCalculator {

    /**
     * Tính giá cuối cùng cho 1 sản phẩm sau khi áp dụng promotion tốt nhất.
     */
    DiscountInfoDto calculateFinalPrice(int productId, int categoryId, BigDecimal price);

    /**
     * Batch: tính giá cho nhiều sản phẩm cùng lúc, tránh N+1 query.
     * Key = productCode, Value = DiscountInfo
     */
    Map<String, DiscountInfoDto> calculateFinalPrices(List<ProductPriceInput> inputs);

    /**
     * Input DTO cho batch calculation.
     */
    record ProductPriceInput(String productCode, String categoryCode, BigDecimal price) {
    }
}
