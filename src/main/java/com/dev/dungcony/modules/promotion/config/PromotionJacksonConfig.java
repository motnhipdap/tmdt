package com.dev.dungcony.modules.promotion.config;

import com.dev.dungcony.modules.promotion.dtos.res.PromotionDetailRes;
import com.dev.dungcony.modules.promotion.dtos.res.PromotionSummaryRes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromotionJacksonConfig {
    public PromotionJacksonConfig(ObjectMapper objectMapper) {
        objectMapper.addMixIn(PromotionSummaryRes.class, PromotionSummaryResMixin.class);
        objectMapper.addMixIn(PromotionDetailRes.class, PromotionDetailResMixin.class);
    }

    private interface PromotionSummaryResMixin {
        @JsonIgnore
        Integer promotionId();
    }

    private interface PromotionDetailResMixin {
        @JsonIgnore
        Integer id();
    }
}
