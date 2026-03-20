package com.dev.dungcony.modules.products.services.interfaces.product;

import com.dev.dungcony.modules.products.dtos.res.ProductDetailRes;
import com.dev.dungcony.modules.products.dtos.res.ProductSummaryRes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductGetService {
    ProductDetailRes getByCode(String code);

    Page<ProductSummaryRes> getAll(Pageable pageable);

    Page<ProductSummaryRes> getAllByCategoryCode(String categoryCode, Pageable pageable);

    Page<ProductSummaryRes> searchByKeyword(String keyword, Pageable pageable);

    Page<ProductSummaryRes> filter(String categoryCode, BigDecimal minPrice, BigDecimal maxPrice,
            String keyword, Pageable pageable);
}
