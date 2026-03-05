package com.dev.dungcony.modules.promotions.services.interfaces;

import java.util.List;

public interface GetIdByCode {
    List<Integer> getByCategoryCodes(List<String> codes);

    Integer getByCategoryCode(String code);

    List<Integer> getByProductCodes(List<String> codes);

    Integer getByProductCode(String code);
}