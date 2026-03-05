package com.dev.dungcony.modules.promotions.config;

import com.dev.dungcony.modules.promotions.dtos.res.PromotionDetailRes;
import com.dev.dungcony.modules.promotions.dtos.res.PromotionSumaryRes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromotionJacksonConfig {
    public PromotionJacksonConfig(ObjectMapper objectMapper) {
        objectMapper.addMixIn(PromotionSumaryRes.class, PromotionSumaryResMixin.class);
        objectMapper.addMixIn(PromotionDetailRes.class, PromotionDetailResMixin.class);
    }

    private interface PromotionSumaryResMixin {
        @JsonIgnore
        Integer promotionId();
    }

    private interface PromotionDetailResMixin {
        @JsonIgnore
        Integer id();
    }
}

