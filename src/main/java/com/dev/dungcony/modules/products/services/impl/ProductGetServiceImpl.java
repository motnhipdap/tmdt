package com.dev.dungcony.modules.products.services.impl;

import com.dev.dungcony.modules.products.dtos.CategorySummaryDto;
import com.dev.dungcony.modules.products.dtos.DiscountInfoDto;
import com.dev.dungcony.modules.products.dtos.ProviderSummaryDto;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailRes;
import com.dev.dungcony.modules.products.dtos.res.ProductSumaryRes;
import com.dev.dungcony.modules.products.entities.Product;
import com.dev.dungcony.modules.products.enums.CategoryStatus;
import com.dev.dungcony.modules.products.enums.ProductStatus;
import com.dev.dungcony.modules.products.exceptions.CategoryNotFoundException;
import com.dev.dungcony.modules.products.exceptions.ProductNotFoundException;
import com.dev.dungcony.modules.products.repositories.CategoryRepository;
import com.dev.dungcony.modules.products.repositories.ProductRepository;
import com.dev.dungcony.modules.products.services.interfaces.ProductGetService;
import com.dev.dungcony.modules.products.services.interfaces.PromotionCalculator;
import com.dev.dungcony.modules.products.services.interfaces.PromotionCalculator.ProductPriceInput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ProductGetServiceImpl implements ProductGetService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PromotionCalculator promotionCalculator;

    @Override
    @Transactional(readOnly = true)
    public ProductDetailRes getById(Integer id) {

        Product product = productRepository.findByIdWithCategoryAndProvider(id)
                .orElseThrow(() -> new ProductNotFoundException("product not found"));

        // Không trả về product đã bị xóa
        if (product.getStatus() == ProductStatus.DELETED) {
            throw new ProductNotFoundException("product not found");
        }

        // Ẩn sản phẩm nếu category bị ẩn
        if (product.getCategory() != null && product.getCategory().getStatus() == CategoryStatus.HIDDEN) {
            throw new ProductNotFoundException("product not found");
        }

        // Tính discount cho sản phẩm
        int categoryId = product.getCategory() != null ? product.getCategory().getId() : 0;
        DiscountInfoDto discount = promotionCalculator.calculateFinalPrice(
                product.getId(), categoryId, product.getPrice());

        CategorySummaryDto catDto = null;
        if (product.getCategory() != null) {
            catDto = new CategorySummaryDto(
                    product.getCategory().getId(),
                    product.getCategory().getName(),
                    product.getCategory().getCategoryCode());
        }
        ProviderSummaryDto provDto = null;
        if (product.getProvider() != null) {
            provDto = new ProviderSummaryDto(
                    product.getProvider().getId(),
                    product.getProvider().getName(),
                    product.getProvider().getProviderCode());
        }

        return new ProductDetailRes(
                product.getId(),
                product.getName(),
                product.getProductCode(),
                product.getDescription(),
                product.getPrice(),
                discount != null ? discount.finalPrice() : product.getPrice(),
                discount != null ? discount.discountType() : "NONE",
                discount != null ? discount.discountValue() : 0,
                product.getQuantity(),
                product.getQuantitySold(),
                product.getRated(),
                product.getImg(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdateAt(),
                catDto,
                provDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSumaryRes> getAll(Pageable pageable) {
        Page<ProductSumaryRes> page = productRepository.findProductList(
                ProductStatus.ACTIVE,
                pageable);

        return enrichWithDiscounts(page);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSumaryRes> getAllByCategoryId(Integer categoryId, Pageable pageable) {
        categoryRepository.findById(categoryId)
                .filter(c -> c.getStatus() == CategoryStatus.ACTIVE)
                .orElseThrow(CategoryNotFoundException::new);

        Page<ProductSumaryRes> rawPage = productRepository.findAllByCategoryTree(categoryId, pageable);

        return enrichWithDiscounts(rawPage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSumaryRes> searchByKeyword(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return getAll(pageable);
        }

        Page<ProductSumaryRes> page = productRepository.getAllByKeyword(
                ProductStatus.ACTIVE,
                keyword.trim(),
                pageable);

        return enrichWithDiscounts(page);
    }

    // ============ PRIVATE HELPERS ============
    private Page<ProductSumaryRes> enrichWithDiscounts(Page<ProductSumaryRes> page) {
        List<ProductSumaryRes> content = page.getContent();
        if (content.isEmpty()) {
            return page;
        }

        // Tạo batch input
        List<ProductPriceInput> inputs = content.stream()
                .map(p -> new ProductPriceInput(
                        p.id(),
                        p.categoryId() != null ? p.categoryId() : 0,
                        p.price()))
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
