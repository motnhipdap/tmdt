package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.DiscountInfoDto;
import com.dev.dungcony.modules.products.dtos.ProductBasicInterface;
import com.dev.dungcony.modules.products.dtos.res.ProductAddRes;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailDto;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailRes;
import com.dev.dungcony.modules.products.dtos.res.ProductImgDto;
import com.dev.dungcony.modules.products.dtos.res.ProductSumaryRes;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.entities.ProductImg;
import com.dev.dungcony.modules.products.enums.ProductStatus;
import com.dev.dungcony.modules.products.exceptions.CategoryNotFoundException;
import com.dev.dungcony.modules.products.exceptions.ProductNotFoundException;
import com.dev.dungcony.modules.products.repositories.CategoryRepository;
import com.dev.dungcony.modules.products.repositories.ProductImgRepository;
import com.dev.dungcony.modules.products.repositories.ProductRepository;
import com.dev.dungcony.modules.products.services.interfaces.ProductGetService;
import com.dev.dungcony.modules.products.services.interfaces.PromotionCalculator;
import com.dev.dungcony.modules.products.services.interfaces.PromotionCalculator.ProductPriceInput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ProductGetServiceImpl implements ProductGetService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PromotionCalculator promotionCalculator;

    @Override
    public ProductDetailRes getById(Integer id) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("product not found")
        );

        List<ProductImgDto> productImgDtos = new ArrayList<>();

        for (ProductImg item : productImgRepository.findByProduct(product)) {
            productImgDtos.add(new ProductImgDto(item.getId(), item.getImageUrl()));
        }

        // Tính discount cho sản phẩm
        int categoryId = product.getCategory() != null ? product.getCategory().getId() : 0;
        DiscountInfoDto discount = promotionCalculator.calculateFinalPrice(
                product.getId(), categoryId, product.getPrice()
        );

        return new ProductDetailDto(product, productImgDtos).withDiscount(discount);
    }

    @Override
    public Page<ProductSumaryRes> getAll(Pageable pageable) {
        Page<ProductSumaryRes> page = productRepository.findProductList(
                ProductStatus.ACTIVE,
                pageable
        );

        return enrichWithDiscounts(page);
    }

    @Override
    public Page<ProductAddRes> getAllByCategoryId(Integer categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException();

        Page<ProductBasicInterface> rawPage =
                productRepository.findAllByCategoryTree(categoryId, pageable);

        Page<ProductAddRes> page = rawPage
                .map(p -> new ProductAddRes(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getRated(),
                        p.getImage(),
                        p.getCategoryId() != null ? p.getCategoryId() : categoryId
                ));

        return enrichWithDiscounts(page);
    }

    // ============ PRIVATE HELPERS ============

    /**
     * Batch tính discount cho tất cả products trong 1 page.
     * Chỉ 3 queries DB bất kể page size.
     */
    private Page<ProductAddRes> enrichWithDiscounts(Page<ProductAddRes> page) {
        List<ProductAddRes> content = page.getContent();
        if (content.isEmpty()) {
            return page;
        }

        // Tạo batch input
        List<ProductPriceInput> inputs = content.stream()
                .map(p -> new ProductPriceInput(
                        p.id(),
                        p.categoryId() != null ? p.categoryId() : 0,
                        p.price()
                ))
                .toList();

        // Batch calculation: 3 queries thay vì 3*N
        Map<Integer, DiscountInfoDto> discountMap = promotionCalculator.calculateFinalPrices(inputs);

        // Map discount vào từng product
        return page.map(p -> {
            DiscountInfoDto discount = discountMap.getOrDefault(p.id(), DiscountInfoDto.noDiscount(p.price()));
            return p.withDiscount(discount);
        });
    }
}
