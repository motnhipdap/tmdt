package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.res.ProductBasicDto;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryService {
    ProductDetailDto getById(Integer id);

    Page<ProductBasicDto> getAll(Pageable pageable);

    Page<ProductBasicDto> getAllByCategoryId(Integer categoryId, Pageable pageable);
}
