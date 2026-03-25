package com.dev.dungcony.modules.cart.services.impl;

import com.dev.dungcony.modules.cart.dtos.req.AddToCartReq;
import com.dev.dungcony.modules.cart.dtos.req.RemoveCartItemReq;
import com.dev.dungcony.modules.cart.dtos.req.UpdateCartItemReq;
import com.dev.dungcony.modules.cart.entities.CartItem;
import com.dev.dungcony.modules.cart.entities.CartItemId;
import com.dev.dungcony.modules.cart.repositories.CartItemRepository;
import com.dev.dungcony.modules.cart.services.interfaces.CartUpdateService;
import com.dev.dungcony.modules.product.entities.Product;
import com.dev.dungcony.modules.product.entities.Size;
import com.dev.dungcony.modules.product.services.interfaces.SizeCacheService;
import com.dev.dungcony.modules.product.services.interfaces.product.ProductGetService;
import com.dev.dungcony.modules.users.entities.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartUpdateImpl implements CartUpdateService {


    private final CartItemRepository cartItemRepository;
    private final SizeCacheService sizeCacheService;
    private final ProductGetService productGetService;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void addItemToCart(AddToCartReq req) {
        Integer productId = productGetService.getIdByCode(req.productCode());
        Integer sizeId = sizeCacheService.getIdBySize(req.size());
        Optional<CartItem> existing = cartItemRepository
                .findById_UserIdAndId_ProductIdAndId_SizeId(req.userId(), productId, sizeId);

        if (existing.isPresent()) {
            CartItem cartItem = existing.get();
            cartItem.setQuantity(cartItem.getQuantity() + req.quantity());
            return;
        }

        CartItem cartItem = new CartItem();
        cartItem.setId(new CartItemId(req.userId(), productId, sizeId));
        cartItem.setUser(entityManager.getReference(User.class, req.userId()));
        cartItem.setProduct(entityManager.getReference(Product.class, productId));
        cartItem.setSize(entityManager.getReference(Size.class, sizeId));
        cartItem.setQuantity(req.quantity());
        cartItem.setIsSelected(false);
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void removeItemFromCart(RemoveCartItemReq req) {
        Integer productId = productGetService.getIdByCode(req.productCode());
        Integer sizeId = sizeCacheService.getIdBySize(req.size());

        CartItem cartItem = new CartItem();

        cartItem.setId(new CartItemId(req.userId(), productId, sizeId));
        cartItem.setUser(entityManager.getReference(User.class, req.userId()));
        cartItem.setProduct(entityManager.getReference(Product.class, productId));
        cartItem.setSize(entityManager.getReference(Size.class, sizeId));
        cartItem.setIsSelected(false);

        cartItemRepository.delete(cartItem);

    }

    @Override
    @Transactional
    public void updateItemQuantity(UpdateCartItemReq req) {
        Integer productId = productGetService.getIdByCode(req.productCode());
        Integer sizeId = sizeCacheService.getIdBySize(req.size());
        CartItemId id = new CartItemId(req.id(), productId, sizeId);
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow();
        cartItem.setQuantity(req.quantity());
    }

}
