package com.dev.dungcony.modules.product.services.impl.product;

import com.dev.dungcony.commons.dtos.DiscountInfoDto;
import com.dev.dungcony.modules.product.dtos.ItemDto;
import com.dev.dungcony.modules.product.dtos.res.ProductDetailRes;
import com.dev.dungcony.modules.product.dtos.res.ProductSummaryRes;
import com.dev.dungcony.modules.product.entities.Product;
import com.dev.dungcony.modules.product.enums.CategoryStatus;
import com.dev.dungcony.modules.product.enums.ProductStatus;
import com.dev.dungcony.modules.product.exceptions.CategoryNotFoundException;
import com.dev.dungcony.modules.product.exceptions.ProductNotFoundException;
import com.dev.dungcony.modules.product.mappers.ProductMapper;
import com.dev.dungcony.modules.product.repositories.CategoryRepository;
import com.dev.dungcony.modules.product.repositories.ProductRepository;
import com.dev.dungcony.modules.product.services.interfaces.item.ItemGetService;
import com.dev.dungcony.modules.product.services.interfaces.product.ProductGetService;
import com.dev.dungcony.modules.promotion.services.interfaces.PromotionCalculator;
import com.dev.dungcony.modules.promotion.services.interfaces.PromotionCalculator.ProductPriceInput;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductGetServiceImpl implements ProductGetService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PromotionCalculator promotionCalculator;
    private final ProductMapper productMapper;

    private final ItemGetService itemGetService;

    @Override
    public Integer getIdByCode(String code) {
        Product product = productRepository.findByCodeWithCategoryAndProvider(code)
                .orElseThrow(() -> new ProductNotFoundException("product not found"));

        // Không trả về product đã bị xóa
        if (product.getStatus() == ProductStatus.DELETED) {
            throw new ProductNotFoundException("product not found");
        }

        // Ẩn sản phẩm nếu category bị ẩn
        if (product.getCategory() != null && product.getCategory().getStatus() == CategoryStatus.HIDDEN) {
            throw new ProductNotFoundException("product not found");
        }

        return product.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailRes getByCode(String code) {

        Product product = productRepository.findByCodeWithCategoryAndProvider(code)
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
        String categoryCode = product.getCategory() != null ? product.getCategory().getCode() : null;
        DiscountInfoDto discount = promotionCalculator.calculateFinalPrice(
                product.getCode(), categoryCode, product.getPrice());

        // Lấy danh sách items (nếu có) để hiển thị chi tiết
        List<ItemDto> items = itemGetService.getByProductCode(product.getCode());

        return productMapper.toDetailRes(product, items, discount);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSummaryRes> getAll(Pageable pageable) {
        Page<ProductSummaryRes> page = productRepository.findProductList(
                ProductStatus.ACTIVE,
                pageable);

        return enrichWithDiscounts(page);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSummaryRes> getAllByCategoryCode(String categoryCode, Pageable pageable) {
        categoryRepository.findByCode(categoryCode)
                .filter(c -> c.getStatus() == CategoryStatus.ACTIVE)
                .orElseThrow(CategoryNotFoundException::new);

        Page<ProductSummaryRes> rawPage = productRepository.findAllByCategoryCode(categoryCode, pageable);

        return enrichWithDiscounts(rawPage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSummaryRes> searchByKeyword(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return getAll(pageable);
        }

        Page<ProductSummaryRes> page = productRepository.getAllByKeyword(
                ProductStatus.ACTIVE,
                keyword.trim(),
                pageable);

        return enrichWithDiscounts(page);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSummaryRes> filter(String categoryCode, BigDecimal minPrice, BigDecimal maxPrice,
                                          String keyword, Pageable pageable) {

        if (categoryCode != null && !categoryCode.isBlank()) {
            categoryRepository.findByCode(categoryCode)
                    .filter(c -> c.getStatus() == CategoryStatus.ACTIVE)
                    .orElseThrow(CategoryNotFoundException::new);
        }

        String trimmedKeyword = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;
        String trimmedCategory = (categoryCode != null && !categoryCode.isBlank()) ? categoryCode.trim() : null;

        Page<ProductSummaryRes> page = productRepository.filterProducts(
                ProductStatus.ACTIVE,
                trimmedCategory,
                minPrice,
                maxPrice,
                trimmedKeyword,
                pageable);

        return enrichWithDiscounts(page);
    }

    // ============ PRIVATE HELPERS ============
    private Page<ProductSummaryRes> enrichWithDiscounts(Page<ProductSummaryRes> page) {
        List<ProductSummaryRes> content = page.getContent();
        if (content.isEmpty()) {
            return page;
        }

        // Tạo batch input
        List<ProductPriceInput> inputs = content.stream()
                .map(p -> new ProductPriceInput(
                        p.code(),
                        p.categoryCode() != null ? p.categoryCode() : null,
                        p.price()))
                .toList();

        // Batch calculation: 3 queries thay vì 3*N
        Map<String, DiscountInfoDto> discountMap = promotionCalculator.calculateFinalPrices(inputs);

        // Map discount vào từng product
        return page.map(p -> {
            DiscountInfoDto discount = discountMap.getOrDefault(
                    p.code(),
                    DiscountInfoDto.noDiscount(p.price()));
            return p.withDiscount(discount);
        });
    }
}
