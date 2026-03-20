package com.dev.dungcony.modules.products.mappers;

import org.springframework.stereotype.Component;

import com.dev.dungcony.modules.products.dtos.ItemDto;
import com.dev.dungcony.modules.products.entities.Item;

@Component
public class ItemMapper {

    public static ItemDto toDto(Item item) {
        return new ItemDto(
                item.getSize().getSize(),
                item.getStatus(),
                item.getQuantity());
    }
}
