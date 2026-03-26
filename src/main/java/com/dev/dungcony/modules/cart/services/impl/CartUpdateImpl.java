package com.dev.dungcony.modules.cart.services.impl;

import com.dev.dungcony.modules.cart.dtos.req.AddToCartReq;
import com.dev.dungcony.modules.cart.dtos.req.UpdateCartItemReq;
import com.dev.dungcony.modules.cart.entities.CartItem;
import com.dev.dungcony.modules.cart.entities.CartItemId;
import com.dev.dungcony.modules.cart.exceptions.CartItemNotFoundException;
import com.dev.dungcony.modules.cart.repositories.CartRepository;
import com.dev.dungcony.modules.cart.services.interfaces.CartUpdateService;
import com.dev.dungcony.modules.product.entities.Product;
import com.dev.dungcony.modules.product.entities.Size;
import com.dev.dungcony.modules.product.enums.ProductSize;
import com.dev.dungcony.modules.product.services.interfaces.SizeCacheService;
import com.dev.dungcony.modules.product.services.interfaces.item.ItemGetService;
import com.dev.dungcony.modules.product.services.interfaces.item.ItemUpdateService;
import com.dev.dungcony.modules.product.services.interfaces.product.ProductGetService;
import com.dev.dungcony.modules.users.entities.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartUpdateImpl implements CartUpdateService {


    private final CartRepository cartItemRepository;
    private final ItemGetService itemGetService;
    private final ItemUpdateService itemUpdateService;
    private final SizeCacheService sizeCacheService;
    private final ProductGetService productGetService;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void addItemToCart(AddToCartReq req) {
        Integer productId = productGetService.getIdByCode(req.productCode());
        Integer sizeId = sizeCacheService.getIdBySize(req.size());

        itemUpdateService.reduce(productId, sizeId, req.quantity());

        CartItem cartItem = new CartItem();
        cartItem.setId(new CartItemId(req.userId(), productId, sizeId));
        cartItem.setUser(entityManager.getReference(User.class, req.userId()));
        cartItem.setProduct(entityManager.getReference(Product.class, productId));
        cartItem.setSize(entityManager.getReference(Size.class, sizeId));
        cartItem.setQuantity(req.quantity());

        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void removeItemFromCart(UUID userId, String productCode, ProductSize size) {
        int productId = productGetService.getIdByCode(productCode);
        int sizeId = sizeCacheService.getIdBySize(size);

        CartItem item = cartItemRepository.findById(new CartItemId(userId, productId, sizeId))
                .orElseThrow(CartItemNotFoundException::new);

        itemUpdateService.increase(productId, sizeId, item.getQuantity());

        cartItemRepository.delete(item);
    }

    @Override
    @Transactional
    public void updateItemQuantity(UpdateCartItemReq req) {
        int productId = productGetService.getIdByCode(req.productCode());
        int sizeId = sizeCacheService.getIdBySize(req.size());
        CartItemId id = new CartItemId(req.id(), productId, sizeId);

        itemUpdateService.reduce(productId, sizeId, req.quantity());

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

        if (req.quantity() <= 0)
            cartItemRepository.delete(cartItem);
        else {
            cartItem.setQuantity(req.quantity());
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    @Transactional
    public void clearCart(UUID userId) {
        cartItemRepository.deleteAllById_UserId(userId);
    }


}
