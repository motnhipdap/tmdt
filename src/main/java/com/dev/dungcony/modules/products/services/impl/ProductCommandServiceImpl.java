package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.req.ProductAddReq;
import com.dev.dungcony.modules.products.dtos.req.ProductUpdateReq;
import com.dev.dungcony.modules.products.dtos.res.ProductAddRes;
import com.dev.dungcony.modules.products.dtos.res.ProductUpdateRes;
import com.dev.dungcony.modules.products.entities.Category;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.entities.Provider;
import com.dev.dungcony.modules.products.enums.ProductStatus;
import com.dev.dungcony.modules.products.exceptions.CategoryNotFoundException;
import com.dev.dungcony.modules.products.exceptions.ProductConfligException;
import com.dev.dungcony.modules.products.exceptions.ProductNotFoundException;
import com.dev.dungcony.modules.products.exceptions.ProviderNotFoundException;
import com.dev.dungcony.modules.products.repositories.CategoryRepository;
import com.dev.dungcony.modules.products.repositories.ProductRepository;
import com.dev.dungcony.modules.products.repositories.ProviderRepository;
import com.dev.dungcony.modules.products.services.interfaces.ProductCommandService;
import com.dev.dungcony.modules.products.services.interfaces.PromotionCalculator;
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
    private final PromotionCalculator promotionCalculator;


    @Transactional
    @Override
    public ProductAddRes addNew(ProductAddReq req) {

        Category cate = categoryRepository.findById(req.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        validateLeaf(cate);

        Provider provider = providerRepository.findById(req.providerId())
                .orElseThrow(ProviderNotFoundException::new);

        Product product = new Product();
        product.setCategory(cate);
        product.setProvider(provider);
        product.setName(req.name());
        product.setDescription(req.description());
        product.setPrice(req.price());
        product.setQuantity(req.quantity());

        productRepository.save(product);

        return new ProductAddRes(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getImg()
        );
    }

    @Transactional
    @Override
    public void delete(int pId) {
        Product product = productRepository.findById(pId)
                .orElseThrow(ProductNotFoundException::new);

        product.setStatus(ProductStatus.DELETED);

        productRepository.save(product);
    }

    @Transactional
    @Override
    public ProductUpdateRes update(ProductUpdateReq req) {

        Product product = productRepository.findById(req.productId())
                .orElseThrow(ProductNotFoundException::new);

        if (product.getStatus() == ProductStatus.DELETED)
            throw new ProductConfligException("product is deleted");

        // ===== CATEGORY =====
        if (req.newCategoryId() != null &&
                !product.getCategory().getId().equals(req.newCategoryId())) {

            Category cate = categoryRepository.findById(req.newCategoryId())
                    .orElseThrow(CategoryNotFoundException::new);

            validateLeaf(cate);

            product.setCategory(cate);
        }

        // ===== PROVIDER =====
        if (req.newProviderId() != null &&
                (product.getProvider() == null ||
                        !product.getProvider().getId().equals(req.newProviderId()))) {

            Provider provider = providerRepository.findById(req.newProviderId())
                    .orElseThrow(ProviderNotFoundException::new);

            product.setProvider(provider);
        }

        // ===== BASIC FIELDS =====
        if (req.newName() != null) product.setName(req.newName());
        if (req.newDesc() != null) product.setDescription(req.newDesc());
        if (req.newPrice() != null) product.setPrice(req.newPrice());

        return new ProductUpdateRes(
                product.getId(),
                product.getCategory().getId(),
                product.getProvider().getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getRated(),
                product.getQuantity(),
                product.getQuantitySold(),
                product.getImg(),
                product.getStatus()
        );
    }

//
//    @Override
//    public void buyProduct(ProductBuyReq req) {
//        Product product = productRepository.findById(req.id())
//                .orElseThrow(ProductNotFoundException::new);
//
//        if (product.getQuantity() < req.quantity())
//            throw new ProductConfligException("INSUFFICIENT_STOCK", "Requested quantity exceeds available stock");
//
//        if (product.getQuantity() == req.quantity())
//            product.setStatus(ProductStatus.OUT_OF_STOCK);
//
//        product.setQuantity(product.getQuantity() - req.quantity());
//        product.setQuantitySold(product.getQuantitySold() + req.quantity());
//
//        productRepository.save(product);
//    }

    @Override
    public void addQuantity(int pId, int quantity) {
        Product product = productRepository.findById(pId)
                .orElseThrow(ProductNotFoundException::new);

        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }

    // check if category is leaf, only leaf category can contain product
    private void validateLeaf(Category cate) {
        if (!cate.getIsLeaf())
            throw new ProductConfligException("Category must be leaf");
    }

}
