package com.dev.dungcony.modules.products.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.products.dtos.req.ProductAddReq;
import com.dev.dungcony.commons.dtos.PageRes;
import com.dev.dungcony.modules.products.dtos.res.ProductBasicDto;
import com.dev.dungcony.modules.products.services.interfaces.ProductQueryService;
import com.dev.dungcony.modules.products.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/product")
public class ProductController {
    private final ProductService productService;
    private final ProductQueryService productQueryService;

    @GetMapping("/get-all")
    public ResponseEntity<ApiRes<?>> getAll(
            @ParameterObject Pageable pageable
    ) {

        Page<ProductBasicDto> productPage = productQueryService.getAll(pageable);

        return ResponseEntity.ok()
                .body(ApiRes.success(
                        "list product",
                        PageRes.from(productPage)
                ));
    }

    @GetMapping("/get-by-category")
    public ResponseEntity<ApiRes<?>> getByCategory(
            @RequestParam("category_id") int categoryId,
            @ParameterObject Pageable pageable
    ) {
        Page<ProductBasicDto> productPage = productQueryService.getAllByCategoryId(categoryId, pageable);

        return ResponseEntity.ok()
                .body(ApiRes.success("list product",
                        PageRes.from(productPage)
                ));
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<ApiRes<?>> getById(
            @RequestParam("id") int id
    ) {
        return ResponseEntity.ok()
                .body(ApiRes.success("product", productQueryService.getById(id)));
    }

    @PostMapping("/addnew")
    public ResponseEntity<ApiRes<?>> addNew(
            @RequestBody ProductAddReq req
    ) {
        productService.addNew(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiRes<?>> update(
            @RequestBody ProductAddReq req
    )
}
