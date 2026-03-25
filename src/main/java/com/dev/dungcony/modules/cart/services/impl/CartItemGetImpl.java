package com.dev.dungcony.modules.cart.services.impl;

import com.dev.dungcony.modules.cart.dtos.CartDto;
import com.dev.dungcony.modules.cart.services.interfaces.CartGetService;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class CartItemGetImpl implements CartGetService {

    @Override
    public CartDto getCart(UUID userId) {
        return null;
    }
}
