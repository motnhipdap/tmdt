package com.dev.dungcony.modules.products.dtos.req;

import java.util.List;

import com.dev.dungcony.modules.products.dtos.ItemDto;

public record IteamAddReq(
                String productCode,
                List<ItemDto> items) {
}
