package com.dev.dungcony.modules.product.services.impl.item;

import com.dev.dungcony.modules.product.dtos.req.ItemUpdateReq;
import com.dev.dungcony.modules.product.entities.Item;
import com.dev.dungcony.modules.product.entities.ItemId;
import com.dev.dungcony.modules.product.enums.ItemStatus;
import com.dev.dungcony.modules.product.exceptions.ItemNotFoundException;
import com.dev.dungcony.modules.product.exceptions.ItemQuantityUnLimit;
import com.dev.dungcony.modules.product.repositories.ItemRepository;
import com.dev.dungcony.modules.product.services.interfaces.GetIdByCode;
import com.dev.dungcony.modules.product.services.interfaces.SizeCacheService;
import com.dev.dungcony.modules.product.services.interfaces.item.ItemUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ItemUpdateimpl implements ItemUpdateService {

    private final ItemRepository itemRepository;
    private final SizeCacheService sizeCacheService;
    private final GetIdByCode getIdByCode;


    @Override
    public void updateQuantity(ItemUpdateReq req) {

        Item item = get(req.productId(), req.sizeId());

        item.setQuantity(req.quantity());
        item.setStatus(req.status());

        itemRepository.save(item);
    }

    @Override
    public void reduce(int productId, int sizeId, int quantity) {

        Item item = get(productId, sizeId);

        int newCnt = item.getQuantity() - quantity;

        if (newCnt < 0)
            throw new ItemQuantityUnLimit();

        if (newCnt == 0)
            item.setStatus(ItemStatus.OUT_OF_STOCK);

        item.setQuantity(newCnt);

        itemRepository.save(item);
    }

    @Override
    public void increase(int productId, int sizeId, int quantity) {

        Item item = get(productId, sizeId);
        int newCnt = item.getQuantity() + quantity;

        item.setQuantity(newCnt);

        itemRepository.save(item);
    }

    private Item get(int pId, int sId) {

        return itemRepository.findById(new ItemId(pId, sId))
                .orElseThrow(ItemNotFoundException::new);

    }
}
