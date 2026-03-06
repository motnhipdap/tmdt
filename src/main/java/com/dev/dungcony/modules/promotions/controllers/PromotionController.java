package com.dev.dungcony.modules.promotions.controllers;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionDetailRes;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionSummaryRes;
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

        @GetMapping("/product/{productCode}")
        public ResponseEntity<ApiRes<List<PromotionSummaryRes>>> getPromotionsByProduct(
                        @PathVariable String productCode) {
                List<PromotionSummaryRes> promotions = promotionProductService.getPromotionByProduct(productCode);
                return ResponseEntity.ok()
                                .body(ApiRes.success("Promotions for product", promotions));
        }

        @GetMapping("/category/{categoryCode}")
        public ResponseEntity<ApiRes<List<PromotionSummaryRes>>> getPromotionsByCategory(
                        @PathVariable String categoryCode) {
                List<PromotionSummaryRes> promotions = promotionCategoryService.getPromotionByCategory(categoryCode);
                return ResponseEntity.ok()
                                .body(ApiRes.success("Promotions for category", promotions));
        }

        @GetMapping("/{code}")
        public ResponseEntity<ApiRes<PromotionDetailRes>> getByCode(
                        @PathVariable String code) {
                return promotionService.getByCode(code)
                                .map(promotion -> ResponseEntity.ok()
                                                .body(ApiRes.success("Promotion detail", promotion)))
                                .orElse(ResponseEntity.notFound().build());
        }

}
