package com.dev.dungcony.modules.products.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.products.dtos.res.PageRes;
import com.dev.dungcony.modules.products.dtos.res.ProductBasicDto;
import com.dev.dungcony.modules.products.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/get-all")
    public ResponseEntity<ApiRes<?>> getAll(
            @ParameterObject Pageable pageable
    ) {

        Page<ProductBasicDto> productPage = productService.getAll(pageable);

        return ResponseEntity.ok()
                .body(ApiRes.success(
                        "list product",
                        PageRes.from(productPage)
                ));
    }


}
