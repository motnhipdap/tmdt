package com.dev.dungcony.modules.promotions.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.promotions.dtos.req.PromoAddReq;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/admin/promotions")
public class AdminController {

    private final PromotionService promotionService;

    @GetMapping("/get-all")
    public ResponseEntity<ApiRes<?>> getAll(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok()
                .body(ApiRes.success("list promotion", promotionService.getAll(pageable)));
    }

    @PostMapping("/add-new")
    public ResponseEntity<Void> addNew(
            @RequestBody PromoAddReq req
    ) {
        URI uri = URI.create("/v1/api/promotions/" + promotionService.addNew(req));

        return ResponseEntity
                .created(uri)
                .build();
    }
    
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<ApiRes<?>> deleteById(
            @RequestBody Integer promotionId
    ) {
        promotionService.delete(promotionId);
        return ResponseEntity.ok().build();
    }

}
