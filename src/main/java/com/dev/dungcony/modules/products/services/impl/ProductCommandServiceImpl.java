package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.req.ProductAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProductUpdateReq;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailRes;
import com.dev.dungcony.modules.products.entities.Category;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.entities.Provider;
import com.dev.dungcony.modules.products.enums.CategoryStatus;
import com.dev.dungcony.modules.products.enums.ProductStatus;
import com.dev.dungcony.modules.products.enums.ProviderStatus;
import com.dev.dungcony.modules.products.exceptions.CategoryNotFoundException;
import com.dev.dungcony.modules.products.exceptions.ProductConflictException;
import com.dev.dungcony.modules.products.exceptions.ProductNotFoundException;
import com.dev.dungcony.modules.products.exceptions.ProviderNotFoundException;
import com.dev.dungcony.modules.products.mappers.ProductMapper;
import com.dev.dungcony.modules.products.repositories.CategoryRepository;
import com.dev.dungcony.modules.products.repositories.ProductRepository;
import com.dev.dungcony.modules.products.repositories.ProviderRepository;
import com.dev.dungcony.modules.products.services.interfaces.ProductCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepository productRepository;
    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Transactional
    @Override
    public ProductDetailRes addNew(ProductAddReq req) {

        Category cate = categoryRepository.findByCode(req.categoryCode())
                .orElseThrow(CategoryNotFoundException::new);

        validateLeaf(cate);
        validateCategoryActive(cate);

        Provider provider = providerRepository.findByCode(req.providerCode())
                .orElseThrow(ProviderNotFoundException::new);

        validateProviderActive(provider);

        Product product = new Product();
        product.setCategory(cate);
        product.setProvider(provider);
        product.setName(req.name());
        product.setCode(generateProductCode(provider.getName(), req.name()));
        product.setDescription(req.description());
        product.setPrice(req.price());
        product.setQuantity(req.quantity());
        product.setImg(req.imgUrl());

        productRepository.save(product);

        return productMapper.toDetailRes(product);
    }

    @Transactional
    @Override
    public void delete(String code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(ProductNotFoundException::new);

        if (product.getStatus() == ProductStatus.DELETED) {
            throw new ProductConflictException("product is already deleted");
        }

        product.setStatus(ProductStatus.DELETED);
    }

    @Transactional
    @Override
    public ProductDetailRes update(ProductUpdateReq req) {

        Product product = productRepository.findByCode(req.productCode())
                .orElseThrow(ProductNotFoundException::new);

        if (product.getStatus() == ProductStatus.DELETED)
            throw new ProductConflictException("product is deleted");

        // ===== CATEGORY =====
        if (req.categoryCode() != null &&
                (product.getCategory() == null || !req.categoryCode().equals(product.getCategory().getCode()))) {

            Category cate = categoryRepository.findByCode(req.categoryCode())
                    .orElseThrow(CategoryNotFoundException::new);

            validateLeaf(cate);
            validateCategoryActive(cate);
            product.setCategory(cate);
        }

        // ===== BASIC FIELDS =====
        if (req.name() != null) {
            product.setName(req.name());
            product.setCode(generateProductCode(product.getName(), req.name()));
        }
        if (req.description() != null)
            product.setDescription(req.description());
        if (req.price() != null)
            product.setPrice(req.price());
        if (req.quantity() != null)
            product.setQuantity(req.quantity());
        if (req.imgUrl() != null)
            product.setImg(req.imgUrl());

        return productMapper.toDetailRes(product);
    }

    @Transactional
    @Override
    public void addQuantity(String code, int quantity) {
        if (quantity == 0)
            return;

        Product product = productRepository.findByCode(code)
                .orElseThrow(ProductNotFoundException::new);

        if (product.getStatus() == ProductStatus.DELETED)
            throw new ProductConflictException("cannot add quantity to deleted product");

        int newQuantity = product.getQuantity() + quantity;
        if (newQuantity < 0)
            throw new ProductConflictException("quantity cannot be negative, current: "
                    + product.getQuantity() + ", requested change: " + quantity);

        product.setQuantity(newQuantity);
    }

    // check if category is leaf, only leaf category can contain product
    private void validateLeaf(Category cate) {
        if (!cate.getIsLeaf())
            throw new ProductConflictException("Category must be leaf");
    }

    private void validateCategoryActive(Category cate) {
        if (cate.getStatus() != CategoryStatus.ACTIVE)
            throw new ProductConflictException("Category is not active");
    }

    private void validateProviderActive(Provider provider) {
        if (provider.getStatus() != ProviderStatus.ACTIVE)
            throw new ProductConflictException("Provider is not active");
    }

    private String generateProductCode(String providerName, String name) {

        String provider = normalize(providerName).substring(0, Math.min(3, providerName.length()));
        String product = normalize(name).replaceAll(" ", "");

        if (product.length() > 6) {
            product = product.substring(0, 6);
        }

        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        return provider + "-" + product + "-" + random;
    }

    private String normalize(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        normalized = normalized.replaceAll("[^a-zA-Z0-9 ]", "");
        return normalized.toUpperCase(Locale.ROOT);
    }
}
