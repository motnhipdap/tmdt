package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.ProductDetailDto;
import com.dev.dungcony.modules.products.dtos.req.ProductAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProductBuyReq;
import com.dev.dungcony.modules.products.dtos.res.ProductBasicDto;
import com.dev.dungcony.modules.products.dtos.res.ProductImgDto;
import com.dev.dungcony.modules.products.entities.Category;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.entities.ProductImg;
import com.dev.dungcony.modules.products.entities.Provider;
import com.dev.dungcony.modules.products.enums.ProductStatus;
import com.dev.dungcony.modules.products.exceptions.*;
import com.dev.dungcony.modules.products.repositories.CategoryRepository;
import com.dev.dungcony.modules.products.repositories.ProductImgRepository;
import com.dev.dungcony.modules.products.repositories.ProductRepsitory;
import com.dev.dungcony.modules.products.repositories.ProviderRepository;
import com.dev.dungcony.modules.products.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductImgRepository productImgRepository;
    private final ProductRepsitory productRepository;
    private final ProviderRepository providerRepository;
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
    public Page<ProductBasicDto> getAll(Pageable pageable) {
        return productRepository.findProductList(
                ProductStatus.ACTIVE,
                pageable
        );
    }

    @Override
    public List<ProductDetailDto> getAllByKeyword(String keyword) {
        return List.of();
    }

    @Override
    public List<ProductBasicDto> getAllByCategoryId(Integer categoryId) {
        return List.of();
    }

    @Transactional
    @Override
    public void addProduct(ProductAddReq req) {

        Category cate = categoryRepository.findById(req.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        if (categoryRepository.existsByParent_Id(cate.getId()))
            throw new ProductNotCreatedException();

        if (!providerRepository.existsById(req.providerId()))
            throw new ProviderNotFoundException();

        Provider provider = new Provider();
        provider.setId(req.providerId());

        Product product = new Product();
        product.setCategory(cate);
        product.setProvider(provider);
        product.setName(req.name());
        product.setDescription(req.description());
        product.setPrice(req.price());
        product.setQuantity(req.quantity());

        productRepository.save(product);
    }

    @Override
    public void buyProduct(ProductBuyReq req) {
        Product product = productRepository.findById(req.id())
                .orElseThrow(ProductNotFoundException::new);

        if (product.getQuantity() < req.quantity())
            throw new ProductConfligException("INSUFFICIENT_STOCK", "Requested quantity exceeds available stock");

        if (product.getQuantity() == req.quantity())
            product.setStatus(ProductStatus.OUT_OF_STOCK);

        product.setQuantity(product.getQuantity() - req.quantity());
        product.setQuantitySold(product.getQuantitySold() + req.quantity());

        productRepository.save(product);
    }

    @Override
    public void updateProduct(ProductDetailDto dto) {
        Product product = productRepository.findById(dto.id())
                .orElseThrow(ProductNotFoundException::new);

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setQuantity(dto.quantity());
        product.setQuantitySold(dto.quantity_sold());

        productRepository.save(product);
    }

}
