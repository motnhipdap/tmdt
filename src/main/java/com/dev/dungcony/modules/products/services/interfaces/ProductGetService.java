package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.res.ProductAddRes;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductGetService {
    ProductDetailDto getById(Integer id);

    Page<ProductAddRes> getAll(Pageable pageable);

    Page<ProductAddRes> getAllByCategoryId(Integer categoryId, Pageable pageable);
}
