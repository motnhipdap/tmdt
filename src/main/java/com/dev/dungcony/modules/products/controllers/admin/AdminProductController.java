package com.dev.dungcony.modules.products.controllers.admin;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.products.dtos.req.*;
import com.dev.dungcony.modules.products.services.interfaces.product.ProductCommandService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/admin/product")
@Tag(name = "Products")
public class AdminProductController {

        private final ProductCommandService productCommandService;

        @PostMapping("/product/add-new")
        public ResponseEntity<ApiRes<?>> addNew(
                        @RequestBody ProductAddReq req) {
                return ResponseEntity.ok()
                                .body(ApiRes.success("Add new product successfully",
                                                productCommandService.addNew(req)));
        }

        @PutMapping("/product/update")
        public ResponseEntity<ApiRes<?>> update(
                        @RequestBody ProductUpdateReq req) {
                return ResponseEntity.ok()
                                .body(ApiRes.success("Update product successfully", productCommandService.update(req)));
        }

        @DeleteMapping("/product/{code}")
        public ResponseEntity<Void> delete(
                        @PathVariable String code) {
                productCommandService.delete(code);
                return ResponseEntity
                                .ok()
                                .build();
        }

}
