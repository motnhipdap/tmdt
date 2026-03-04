package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.res.ProductDetailRes;
import com.dev.dungcony.modules.products.dtos.res.ProductSumaryRes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductGetService {
    ProductDetailRes getById(Integer id);

    Page<ProductSumaryRes> getAll(Pageable pageable);

    Page<ProductSumaryRes> getAllByCategoryId(Integer categoryId, Pageable pageable);

    Page<ProductSumaryRes> searchByKeyword(String keyword, Pageable pageable);
}
