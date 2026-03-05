package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.res.ProductDetailRes;
import com.dev.dungcony.modules.products.dtos.res.ProductSummaryRes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductGetService {
    ProductDetailRes getByCode(String code);

    Page<ProductSummaryRes> getAll(Pageable pageable);

    Page<ProductSummaryRes> getAllByCategoryCode(String categoryCode, Pageable pageable);

    Page<ProductSummaryRes> searchByKeyword(String keyword, Pageable pageable);
}
