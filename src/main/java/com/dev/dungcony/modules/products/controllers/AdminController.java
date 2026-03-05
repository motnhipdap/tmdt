package com.dev.dungcony.modules.products.controllers;

import com.dev.dungcony.commons.bases.ControllerBase;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.products.dtos.req.*;
import com.dev.dungcony.modules.products.services.interfaces.CategoryCommandService;
import com.dev.dungcony.modules.products.services.interfaces.ProductCommandService;
import com.dev.dungcony.modules.products.services.interfaces.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("v1/api/admin/")
public class AdminController extends ControllerBase {

    private final ProductCommandService productCommandService;
    private final CategoryCommandService categoryCommandService;
    private final ProviderService providerService;

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
    public ResponseEntity<Void> deleteProduct(
            @PathVariable int id
    ) {
        productCommandService.delete(id);
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/category/add-new")
    public ResponseEntity<ApiRes<?>> addNew(
            @RequestBody CategoryAddReq req
    ) {
        return ResponseEntity.ok()
                .body(ApiRes.success("Add new product successfully", categoryCommandService.addNew(req)));
    }


    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCate(
            @PathVariable int id
    ) {
        productCommandService.delete(id);
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/provider/add-new")
    public ResponseEntity<ApiRes<?>> addProvider(
            @RequestBody ProviderAddReq req
    ) {
        return ResponseEntity.ok()
                .body(ApiRes.success("Add new product successfully", providerService.addNew(req)));
    }

    @PutMapping("/provider/update")
    public ResponseEntity<ApiRes<?>> update(
            @RequestBody ProviderUpdateReq req
    ) {
        return ResponseEntity.ok()
                .body(ApiRes.success("Update product successfully", providerService.update(req)));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProvider(
            @PathVariable int id
    ) {
        productCommandService.delete(id);
        return ResponseEntity
                .ok()
                .build();
    }

}
