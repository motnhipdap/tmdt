package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.req.ProductAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProductUpdateReq;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailRes;
import com.dev.dungcony.modules.products.dtos.CategorySummaryDto;
import com.dev.dungcony.modules.products.dtos.ProviderSummaryDto;
import com.dev.dungcony.modules.products.dtos.res.ProductSumaryRes;
import com.dev.dungcony.modules.products.entities.Category;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.entities.Provider;
import com.dev.dungcony.modules.products.enums.CategoryStatus;
import com.dev.dungcony.modules.products.enums.ProductStatus;
import com.dev.dungcony.modules.products.enums.ProviderStatus;
import com.dev.dungcony.modules.products.exceptions.CategoryNotFoundException;
import com.dev.dungcony.modules.products.exceptions.ProductConfligException;
import com.dev.dungcony.modules.products.exceptions.ProductNotFoundException;
import com.dev.dungcony.modules.products.exceptions.ProviderNotFoundException;
import com.dev.dungcony.modules.products.repositories.CategoryRepository;
import com.dev.dungcony.modules.products.repositories.ProductRepository;
import com.dev.dungcony.modules.products.repositories.ProviderRepository;
import com.dev.dungcony.modules.products.services.interfaces.ProductCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductCommandServiceImpl implements ProductCommandService {

    private final ProductRepository productRepository;
    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public ProductDetailRes addNew(ProductAddReq req) {

        Category cate = categoryRepository.findById(req.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        validateLeaf(cate);
        validateCategoryActive(cate);

        Provider provider = providerRepository.findById(req.providerId())
                .orElseThrow(ProviderNotFoundException::new);

        validateProviderActive(provider);

        Product product = new Product();
        product.setCategory(cate);
        product.setProvider(provider);
        product.setName(req.name());
        product.setProductCode(req.productCode());
        product.setDescription(req.description());
        product.setPrice(req.price());
        product.setQuantity(req.quantity());
        product.setImg(req.imgUrl());

        productRepository.save(product);

        return toDetailRes(product);
    }

    @Transactional
    @Override
    public void delete(int pId) {
        Product product = productRepository.findById(pId)
                .orElseThrow(ProductNotFoundException::new);

        if (product.getStatus() == ProductStatus.DELETED) {
            throw new ProductConfligException("product is already deleted");
        }

        product.setStatus(ProductStatus.DELETED);
    }

    @Transactional
    @Override
    public ProductDetailRes update(ProductUpdateReq req) {

        CategorySummaryDto catDto = null;
        ProviderSummaryDto provDto = null;

        Product product = productRepository.findById(req.productId())
                .orElseThrow(ProductNotFoundException::new);

        if (product.getStatus() == ProductStatus.DELETED)
            throw new ProductConfligException("product is deleted");

        // ===== CATEGORY =====
        if (req.categoryId() != null &&
                (product.getCategory() == null || !req.categoryId().equals(product.getCategory().getId()))) {

            Category cate = categoryRepository.findById(req.categoryId())
                    .orElseThrow(CategoryNotFoundException::new);

            validateLeaf(cate);
            validateCategoryActive(cate);
            product.setCategory(cate);

            catDto = new CategorySummaryDto(
                    cate.getId(),
                    cate.getName(),
                    cate.getCategoryCode());
        }

        // ===== PROVIDER =====
        if (req.providerId() != null &&
                (product.getProvider() == null ||
                        !req.providerId().equals(product.getProvider().getId()))) {

            Provider provider = providerRepository.findById(req.providerId())
                    .orElseThrow(ProviderNotFoundException::new);

            validateProviderActive(provider);
            product.setProvider(provider);
            provDto = new ProviderSummaryDto(
                    provider.getId(),
                    provider.getName(),
                    provider.getProviderCode()
            );
        }

        // ===== BASIC FIELDS =====
        if (req.name() != null)
            product.setName(req.name());
        if (req.description() != null)
            product.setDescription(req.description());
        if (req.price() != null)
            product.setPrice(req.price());
        if (req.quantity() != null)
            product.setQuantity(req.quantity());
        if (req.imgUrl() != null)
            product.setImg(req.imgUrl());

        return new ProductDetailRes(
                product.getId(),

                product.getName(),
                product.getProductCode(),
                product.getDescription(),

                product.getPrice(),
                product.getPrice(),

                null,
                0,

                product.getQuantity(),
                product.getQuantitySold(),

                product.getRated(),

                product.getImg(),
                product.getStatus(),

                product.getCreatedAt(),
                product.getUpdateAt(),

                catDto,
                provDto
        );
    }

    @Transactional
    @Override
    public void addQuantity(int pId, int quantity) {
        if (quantity == 0)
            return;

        Product product = productRepository.findById(pId)
                .orElseThrow(ProductNotFoundException::new);

        if (product.getStatus() == ProductStatus.DELETED)
            throw new ProductConfligException("cannot add quantity to deleted product");

        int newQuantity = product.getQuantity() + quantity;
        if (newQuantity < 0)
            throw new ProductConfligException("quantity cannot be negative, current: "
                    + product.getQuantity() + ", requested change: " + quantity);

        product.setQuantity(newQuantity);
    }

    // check if category is leaf, only leaf category can contain product
    private void validateLeaf(Category cate) {
        if (!cate.getIsLeaf())
            throw new ProductConfligException("Category must be leaf");
    }

    private void validateCategoryActive(Category cate) {
        if (cate.getStatus() != CategoryStatus.ACTIVE)
            throw new ProductConfligException("Category is not active");
    }

    private void validateProviderActive(Provider provider) {
        if (provider.getStatus() != ProviderStatus.ACTIVE)
            throw new ProductConfligException("Provider is not active");
    }

    private ProductDetailRes toDetailRes(Product p) {
        CategorySummaryDto catDto = null;
        if (p.getCategory() != null) {
            Category c = p.getCategory();
            catDto = new CategorySummaryDto(c.getId(), c.getName(), c.getCategoryCode());
        }
        ProviderSummaryDto provDto = null;
        if (p.getProvider() != null) {
            Provider pv = p.getProvider();
            provDto = new ProviderSummaryDto(pv.getId(), pv.getName(), pv.getProviderCode());
        }
        return new ProductDetailRes(
                p.getId(),
                p.getName(),
                p.getProductCode(),
                p.getDescription(),
                p.getPrice(),
                p.getPrice(),
                "NONE",
                0,
                p.getQuantity(),
                p.getQuantitySold(),
                p.getRated(),
                p.getImg(),
                p.getStatus(),
                p.getCreatedAt(),
                p.getUpdateAt(),
                catDto,
                provDto);
    }
}
