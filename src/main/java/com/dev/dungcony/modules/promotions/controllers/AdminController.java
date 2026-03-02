package com.dev.dungcony.modules.promotions.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.promotions.dtos.req.PromoAddReq;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionService;
import jakarta.validation.Valid;
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
            @Valid @RequestBody PromoAddReq req
    ) {
        URI uri = URI.create("/v1/api/promotions/" + promotionService.addNew(req));

        return ResponseEntity
                .created(uri)
                .build();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiRes<?>> update(
            @Valid @RequestBody com.dev.dungcony.modules.promotions.dtos.req.PromoUpdateReq req
    ) {
        promotionService.update(req);
        return ResponseEntity.ok()
                .body(ApiRes.success("Promotion updated successfully", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Integer id
    ) {
        promotionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
