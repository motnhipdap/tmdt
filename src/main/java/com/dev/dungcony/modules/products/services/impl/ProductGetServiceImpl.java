package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.ProductBasicInterface;
import com.dev.dungcony.modules.products.dtos.res.ProductAddRes;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailDto;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.enums.ProductStatus;
import com.dev.dungcony.modules.products.exceptions.CategoryNotFoundException;
import com.dev.dungcony.modules.products.exceptions.ProductNotFoundException;
import com.dev.dungcony.modules.products.repositories.CategoryRepository;
import com.dev.dungcony.modules.products.repositories.ProductRepository;
import com.dev.dungcony.modules.products.services.interfaces.ProductGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductGetServiceImpl implements ProductGetService {
    private final ProductRepository productRepository;
    private final ProductImgRepository productImgRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDetailDto getById(Integer id) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("product not found")
        );

        List<ProductImgDto> productImgDtos = new ArrayList<>();

        for (ProductImg item : productImgRepository.findByProduct(product)) {
            productImgDtos.add(new ProductImgDto(item.getId(), item.getImageUrl()));
        }

        return new ProductDetailDto(product, productImgDtos);
    }

    @Override
    public Page<ProductAddRes> getAll(Pageable pageable) {
        return productRepository.findProductList(
                ProductStatus.ACTIVE,
                pageable
        );
    }

    @Override
    public Page<ProductAddRes> getAllByCategoryId(Integer categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        Page<ProductBasicInterface> page =
                productRepository.findAllByCategoryTree(categoryId, pageable);
        return page
                .map(p -> new ProductAddRes(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getRated(),
                        p.getImage()
                ));
    }

}
