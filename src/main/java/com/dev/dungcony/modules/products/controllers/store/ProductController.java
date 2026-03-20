package com.dev.dungcony.modules.products.controllers.store;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.commons.dtos.PageRes;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailRes;
import com.dev.dungcony.modules.products.dtos.res.ProductSummaryRes;
import com.dev.dungcony.modules.products.services.interfaces.product.ProductGetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/public/product")
@Tag(name = "Products")
public class ProductController {
        private final ProductGetService productQueryService;

        @Operation(summary = "Lấy danh sách sản phẩm", description = "Phân trang, hỗ trợ sort: ?page=0&size=10&sort=price,asc")
        @GetMapping("/get-all")
        public ResponseEntity<ApiRes<PageRes<ProductSummaryRes>>> getAll(
                        @ParameterObject Pageable pageable) {

                Page<ProductSummaryRes> productPage = productQueryService.getAll(pageable);

                return ResponseEntity.ok()
                                .body(ApiRes.success(
                                                "list product",
                                                PageRes.from(productPage)));
        }

        @Operation(summary = "Lấy sản phẩm theo danh mục", description = "Bao gồm cả sản phẩm trong sub-categories")
        @GetMapping("/get-by-category")
        public ResponseEntity<ApiRes<PageRes<ProductSummaryRes>>> getByCategory(
                        @Parameter(description = "Mã danh mục") @RequestParam("category_code") String categoryCode,
                        @ParameterObject Pageable pageable) {
                Page<ProductSummaryRes> productPage = productQueryService.getAllByCategoryCode(categoryCode, pageable);

                return ResponseEntity.ok()
                                .body(ApiRes.success("list product",
                                                PageRes.from(productPage)));
        }

        @Operation(summary = "Xem chi tiết sản phẩm")
        @GetMapping("/get-by-code")
        public ResponseEntity<ApiRes<ProductDetailRes>> getById(
                        @Parameter(description = "Mã sản phẩm") @RequestParam("code") String code) {
                return ResponseEntity.ok()
                                .body(ApiRes.success("product", productQueryService.getByCode(code)));
        }

        @Operation(summary = "Tìm kiếm sản phẩm", description = "Tìm theo tên hoặc mô tả sản phẩm")
        @GetMapping("/search")
        public ResponseEntity<ApiRes<PageRes<ProductSummaryRes>>> search(
                        @Parameter(description = "Từ khóa tìm kiếm") @RequestParam("keyword") String keyword,
                        @ParameterObject Pageable pageable) {
                Page<ProductSummaryRes> productPage = productQueryService.searchByKeyword(keyword, pageable);
                return ResponseEntity.ok()
                                .body(ApiRes.success("search results",
                                                PageRes.from(productPage)));
        }

}
