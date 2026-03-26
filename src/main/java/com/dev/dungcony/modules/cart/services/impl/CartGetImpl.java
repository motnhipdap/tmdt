package com.dev.dungcony.modules.cart.services.impl;

import com.dev.dungcony.modules.cart.dtos.res.CartRes;
import com.dev.dungcony.modules.cart.entities.CartItem;
import com.dev.dungcony.modules.cart.mappers.CartMapper;
import com.dev.dungcony.modules.cart.repositories.CartRepository;
import com.dev.dungcony.modules.cart.services.interfaces.CartGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class CartGetImpl implements CartGetService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public CartRes getCart(UUID userId) {
        List<CartItem> items = cartRepository.findAllByUserId(userId);
        return cartMapper.toCartRes(items);
    }
}
