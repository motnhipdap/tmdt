package com.dev.dungcony.modules.products.services.impl.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dev.dungcony.modules.products.dtos.ItemDto;
import com.dev.dungcony.modules.products.dtos.req.IteamAddReq;
import com.dev.dungcony.modules.products.entities.Item;
import com.dev.dungcony.modules.products.entities.ItemId;
import com.dev.dungcony.modules.products.repositories.ItemRepository;
import com.dev.dungcony.modules.products.services.interfaces.GetIdByCode;
import com.dev.dungcony.modules.products.services.interfaces.SizeCacheService;
import com.dev.dungcony.modules.products.services.interfaces.item.ItemCreateService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemCreateImpl implements ItemCreateService {

    private final ItemRepository itemRepository;
    private final GetIdByCode getIdByCode;
    private final SizeCacheService sizeCacheService;

    @Override
    public List<String> createItems(IteamAddReq items) {

        Integer productId = getIdByCode.getByProductCode(items.productCode());

        List<Item> tmp = new ArrayList<>();

        for (ItemDto item : items.items()) {
            ItemId id = new ItemId(
                    productId,
                    sizeCacheService.getIdBySize(item.size()));

            tmp.add(new Item(
                    id,
                    item.quantity()));
        }

        List<Item> savedItems = itemRepository.saveAll(tmp);

        return savedItems.stream()
                .map(item -> items.productCode() + "/size/" + item.getId())
                .collect(Collectors.toList());
    }
}
