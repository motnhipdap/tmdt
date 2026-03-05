package com.dev.dungcony.modules.products.controllers;

import com.dev.dungcony.commons.bases.ControllerBase;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.commons.dtos.PageRes;
import com.dev.dungcony.modules.products.dtos.res.ProductDetailRes;
import com.dev.dungcony.modules.products.dtos.res.ProductSumaryRes;
import com.dev.dungcony.modules.products.services.interfaces.ProductGetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("v1/api/public/product")
@Tag(name = "Product", description = "Quản lý sản phẩm")
public class ProductController extends ControllerBase {

    private final ProductGetService productQueryService;

    @Operation(summary = "Lấy danh sách sản phẩm", description = "Phân trang, hỗ trợ sort: ?page=0&size=10&sort=price,asc")
    @GetMapping("/get-all")
    public ResponseEntity<ApiRes<PageRes<ProductSumaryRes>>> getAll(
            @ParameterObject Pageable pageable) {

        Page<ProductSumaryRes> productPage = productQueryService.getAll(pageable);

        return ResponseEntity.ok()
                .body(ApiRes.success(
                        "list product",
                        PageRes.from(productPage)));
    }

    @Operation(summary = "Lấy sản phẩm theo danh mục", description = "Bao gồm cả sản phẩm trong sub-categories")
    @GetMapping("/get-by-category")
    public ResponseEntity<ApiRes<PageRes<ProductSumaryRes>>> getByCategory(
            @Parameter(description = "ID danh mục") @RequestParam("category_id") int categoryId,
            @ParameterObject Pageable pageable) {
        Page<ProductSumaryRes> productPage = productQueryService.getAllByCategoryId(categoryId, pageable);

        return ResponseEntity.ok()
                .body(ApiRes.success("list product",
                        PageRes.from(productPage)));
    }

    @Operation(summary = "Xem chi tiết sản phẩm")
    @GetMapping("/get-by-id")
    public ResponseEntity<ApiRes<ProductDetailRes>> getById(
            @Parameter(description = "ID sản phẩm") @RequestParam("id") int id) {
        return ResponseEntity.ok()
                .body(ApiRes.success("product", productQueryService.getById(id)));
    }

    @Operation(summary = "Tìm kiếm sản phẩm", description = "Tìm theo tên hoặc mô tả sản phẩm")
    @GetMapping("/search")
    public ResponseEntity<ApiRes<PageRes<ProductSumaryRes>>> search(
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam("keyword") String keyword,
            @ParameterObject Pageable pageable) {
        Page<ProductSumaryRes> productPage = productQueryService.searchByKeyword(keyword, pageable);
        return ResponseEntity.ok()
                .body(ApiRes.success("search results",
                        PageRes.from(productPage)));
    }

}
