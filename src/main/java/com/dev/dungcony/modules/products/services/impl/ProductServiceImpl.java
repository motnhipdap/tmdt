package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.req.ProductAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProductBuyReq;
import com.dev.dungcony.modules.products.dtos.req.ProductUpdateReq;
import com.dev.dungcony.modules.products.dtos.res.ProductBasicDto;
import com.dev.dungcony.modules.products.entities.Category;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.entities.Provider;
import com.dev.dungcony.modules.products.enums.ProductStatus;
import com.dev.dungcony.modules.products.exceptions.*;
import com.dev.dungcony.modules.products.repositories.CategoryRepository;
import com.dev.dungcony.modules.products.repositories.ProductImgRepository;
import com.dev.dungcony.modules.products.repositories.ProductRepository;
import com.dev.dungcony.modules.products.repositories.ProviderRepository;
import com.dev.dungcony.modules.products.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImgRepository productImgRepository;


    @Transactional
    @Override
    public void addNew(ProductAddReq req) {

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
    public void addQuantity(int pId, int quantity) {
        Product product = productRepository.findById(pId)
                .orElseThrow(ProductNotFoundException::new);

        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }

    @Override
    public void delete(int pId) {
        Product product = productRepository.findById(pId)
                .orElseThrow(ProductNotFoundException::new);

        product.setStatus(ProductStatus.DELETED);

        productRepository.save(product);
    }

    @Transactional
    @Override
    public ProductBasicDto update(ProductUpdateReq req) {

        Product product = productRepository.findById(req.productId())
                .orElseThrow(ProductNotFoundException::new);

        Category cate = categoryRepository.findById(req.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        if (product.getCategory() != null && !product.getCategory().getId().equals(req.categoryId()))
            if (categoryRepository.existsByParent_Id(cate.getId()))
                throw new ProductConfligException("category is not valid");

        if (!product.getProvider().getId().equals(req.providerId()))
            if (!providerRepository.existsById(req.providerId()))
                throw new ProviderNotFoundException();

        Provider provider = new Provider();
        provider.setId(req.providerId());

        product.setName(req.newName());
        product.setDescription(req.newDesc());
        product.setPrice(req.newPrice());
        product.setStatus(req.newStatus());
        product.setCategory(cate);
        product.setProvider(provider);

        productRepository.save(product);

        return new ProductBasicDto(product, productRepository.findMainImage(product.getId()).orElse(null));
        
    }
}
