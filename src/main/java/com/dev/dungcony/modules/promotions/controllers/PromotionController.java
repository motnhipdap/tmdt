package com.dev.dungcony.modules.promotions.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionDetailRes;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionSumaryRes;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionCategoryService;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionProductService;
import com.dev.dungcony.modules.promotions.services.interfaces.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;
    private final PromotionProductService promotionProductService;
    private final PromotionCategoryService promotionCategoryService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiRes<List<PromotionSumaryRes>>> getPromotionsByProduct(
            @PathVariable Integer productId
    ) {
        List<PromotionSumaryRes> promotions = promotionProductService.getPromotionByProduct(productId);
        return ResponseEntity.ok()
                .body(ApiRes.success("Promotions for product", promotions));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiRes<List<PromotionSumaryRes>>> getPromotionsByCategory(
            @PathVariable Integer categoryId
    ) {
        List<PromotionSumaryRes> promotions = promotionCategoryService.getPromotionByCategory(categoryId);
        return ResponseEntity.ok()
                .body(ApiRes.success("Promotions for category", promotions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiRes<PromotionDetailRes>> getById(
            @PathVariable Integer id
    ) {
        return promotionService.getById(id)
                .map(promotion -> ResponseEntity.ok()
                        .body(ApiRes.success("Promotion detail", promotion)))
                .orElse(ResponseEntity.notFound().build());
    }

}
