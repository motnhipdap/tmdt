package com.dev.dungcony.modules.products.controllers.admin;

import com.dev.dungcony.commons.bases.ControllerBase;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.products.dtos.req.*;
import com.dev.dungcony.modules.products.services.interfaces.ProductCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("v1/api/admin/product")
@Tag(name = "Product", description = "Quản lý sản phẩm")
public class AdminProductController {

    private final ProductCommandService productCommandService;


    @PostMapping("/product/add-new")
    public ResponseEntity<ApiRes<?>> addNew(
            @RequestBody ProductAddReq req
    ) {
        return ResponseEntity.ok()
                .body(ApiRes.success("Add new product successfully", productCommandService.addNew(req)));
    }

    @PutMapping("/product/update")
    public ResponseEntity<ApiRes<?>> update(
            @RequestBody ProductUpdateReq req
    ) {
        return ResponseEntity.ok()
                .body(ApiRes.success("Update product successfully", productCommandService.update(req)));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable int id
    ) {
        productCommandService.delete(id);
        return ResponseEntity
                .ok()
                .build();
    }

}
