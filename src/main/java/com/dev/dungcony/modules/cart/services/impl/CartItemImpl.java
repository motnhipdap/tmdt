package com.dev.dungcony.modules.cart.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.dungcony.modules.cart.dtos.req.AddToCartReq;
import com.dev.dungcony.modules.cart.dtos.req.UpdateCartItemReq;
import com.dev.dungcony.modules.cart.entities.CartItem;
import com.dev.dungcony.modules.cart.entities.CartItemId;
import com.dev.dungcony.modules.cart.exceptions.CartItemNotFoundException;
import com.dev.dungcony.modules.cart.mappers.CartMapper;
import com.dev.dungcony.modules.cart.repositories.CartItemRepository;
import com.dev.dungcony.modules.cart.services.interfaces.CartGetService;
import com.dev.dungcony.modules.cart.services.interfaces.CartRemoveService;
import com.dev.dungcony.modules.product.entities.Product;
import com.dev.dungcony.modules.product.entities.Size;
import com.dev.dungcony.modules.product.exceptions.ProductNotFoundException;
import com.dev.dungcony.modules.product.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemImpl implements CartRemoveService {

    private final CartItemRepository cartItemRepository;
    private final CartGetService cartGetService;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public void addItemToCart(AddToCartReq req) {
        CartRes cart = cartGetService.getCart(req.cartId());

        Product product = productRepository.findByCode(req.productCode())
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + req.productCode()));

        Optional<CartItem> existing = cartItemRepository
                .findById_CartIdAndId_ProductIdAndId_SizeId(cart.getId(), product.getId(), req.sizeId());

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + req.quantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setId(new CartItemId(cart.getId(), product.getId(), req.sizeId()));
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            Size size = new Size();
            size.setId(req.sizeId());
            cartItem.setSize(size);
            cartItem.setQuantity(req.quantity());
            cartItem.setIsSelected(false);
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    @Transactional
    public void removeItemFromCart(UUID userId, String productCode, Integer sizeId) {
        Cart cart = cartGetService.getCartByUserId(userId);

        Product product = productRepository.findByCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productCode));

        cartItemRepository.deleteByCartIdAndProductIdAndSizeId(cart.getId(), product.getId(), sizeId);
    }

    @Override
    @Transactional
    public void updateItemQuantity(UUID userId, UpdateCartItemReq req) {
        Cart cart = cartGetService.getCartByUserId(userId);

        Product product = productRepository.findByCode(req.productCode())
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + req.productCode()));

        CartItem item = cartItemRepository
                .findById_CartIdAndId_ProductIdAndId_SizeId(cart.getId(), product.getId(), req.sizeId())
                .orElseThrow(CartItemNotFoundException::new);

        item.setQuantity(req.quantity());
    }

    @Override
    @Transactional
    public void toggleItemSelection(UUID userId, String productCode, Integer sizeId) {
        Cart cart = cartGetService.getCartByUserId(userId);

        Product product = productRepository.findByCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productCode));

        CartItem item = cartItemRepository
                .findById_CartIdAndId_ProductIdAndId_SizeId(cart.getId(), product.getId(), sizeId)
                .orElseThrow(CartItemNotFoundException::new);

        item.setIsSelected(!item.getIsSelected());
    }

    @Override
    @Transactional
    public void toggleSelectAll(UUID userId, boolean selected) {
        Cart cart = cartGetService.getCartByUserId(userId);
        cartItemRepository.updateAllSelectionByCartId(cart.getId(), selected);
    }

    @Override
    @Transactional
    public void clearCart(Integer cartId) {
        cartItemRepository.deleteAllByCartId(cartId);
    }

    @Override
    public List<CartItemRes> getCartItems(Integer cartId) {
        List<CartItem> items = cartItemRepository.findAllByCartIdWithDetails(cartId);
        return items.stream()
                .map(cartMapper::toCartItemRes)
                .toList();
    }

}
