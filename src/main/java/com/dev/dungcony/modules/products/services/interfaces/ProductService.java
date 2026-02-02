package com.dev.dungcony.modules.products.services.interfaces;

import com.dev.dungcony.modules.products.dtos.ProductDetailDto;
import com.dev.dungcony.modules.products.dtos.req.ProductAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProductBuyReq;
import com.dev.dungcony.modules.products.dtos.res.ProductBasicDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductDetailDto getById(Integer id);

    Page<ProductBasicDto> getAll(Pageable pageable);

    List<ProductDetailDto> getAllByKeyword(String keyword);

    List<ProductBasicDto> getAllByCategoryId(Integer categoryId);

    void addProduct(ProductAddReq req);

    void buyProduct(ProductBuyReq req);

    void updateProduct(ProductDetailDto dto);
}
