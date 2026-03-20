package com.dev.dungcony.modules.products.services.impl.item;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dev.dungcony.modules.products.dtos.ItemDto;
import com.dev.dungcony.modules.products.entities.ItemId;
import com.dev.dungcony.modules.products.enums.ProductSize;
import com.dev.dungcony.modules.products.mappers.ItemMapper;
import com.dev.dungcony.modules.products.repositories.ItemRepository;
import com.dev.dungcony.modules.products.services.interfaces.GetIdByCode;
import com.dev.dungcony.modules.products.services.interfaces.SizeCacheService;
import com.dev.dungcony.modules.products.services.interfaces.item.ItemGetService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemGetImpl implements ItemGetService {

    private final ItemRepository itemRepository;
    private final SizeCacheService sizeCacheService;
    private final GetIdByCode getIdByCode;

    @Override
    public List<ItemDto> getByProductId(Integer productId) {
        return itemRepository.findByIdProductId(productId).stream().map(ItemMapper::toDto).toList();
    }

    @Override
    public List<ItemDto> getByProductCode(String productCode) {
        Integer productId = getIdByCode.getByProductCode(productCode);
        return itemRepository.findByIdProductId(productId).stream().map(ItemMapper::toDto).toList();
    }

    @Override
    public List<ItemDto> getBySizeId(Integer sizeId) {
        return itemRepository.findByIdSizeId(sizeId).stream().map(ItemMapper::toDto).toList();
    }

    @Override
    public List<ProductSize> getSizesByProductCode(String productCode) {

        List<Integer> sizeIds = itemRepository.findSizesByProductCode(productCode);

        return sizeIds.stream().map(sizeCacheService::getProductSizeById).toList();
    }

    @Override
    public ItemDto getByProductCodeAndSizeId(String productCode, Integer sizeId) {
        Integer productId = getIdByCode.getByProductCode(productCode);

        return itemRepository.findById(new ItemId(productId, sizeId))
                .map(ItemMapper::toDto)
                .orElse(null);
    }
}
