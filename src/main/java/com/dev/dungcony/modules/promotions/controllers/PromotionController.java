package com.dev.dungcony.modules.promotions.controllers;

import com.dev.dungcony.modules.promotions.services.interfaces.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

}
