package com.dev.dungcony.modules.products.controllers.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.products.dtos.req.IteamAddReq;
import com.dev.dungcony.modules.products.services.interfaces.item.ItemCreateService;
import com.dev.dungcony.modules.products.services.interfaces.item.ItemGetService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/admin/product")
@Tag(name = "Products")
public class AdminItemController {

    private final ItemCreateService itemCreateService;
    private final ItemGetService service;

    @RequestMapping("/add-items")
    public ResponseEntity<ApiRes<List<String>>> addItems(
            @RequestBody IteamAddReq req) {

        return ResponseEntity.status(201).body(ApiRes.success(
                "created",
                itemCreateService.createItems(req)));
    }

    @Operation(summary = "lấy toàn bộ size của sản phẩm")
    @RequestMapping("/items")
    public ResponseEntity<ApiRes<?>> getItems(
            @RequestParam String code) {
        return ResponseEntity.ok()
                .body(ApiRes.success(
                        "list items",
                        service.getByProductCode(code)));
    }

}
