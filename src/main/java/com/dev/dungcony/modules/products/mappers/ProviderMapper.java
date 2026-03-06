package com.dev.dungcony.modules.products.mappers;

import com.dev.dungcony.modules.products.dtos.ProviderSummaryDto;
import com.dev.dungcony.modules.products.dtos.res.ProviderRes;
import com.dev.dungcony.modules.products.entities.Provider;
import org.springframework.stereotype.Component;

/**
 * Mapper chuyển đổi giữa Provider entity và các DTO response.
 * Không bao gồm internal id — client dùng code làm định danh.
 */
@Component
public class ProviderMapper {

    public ProviderRes toRes(Provider p) {
        return new ProviderRes(
                p.getName(),
                p.getCode(),
                p.getEmail(),
                p.getPhone(),
                p.getDescription(),
                p.getLogo());
    }

    public ProviderSummaryDto toSummaryDto(Provider p) {
        return new ProviderSummaryDto(p.getName(), p.getCode());
    }
}
