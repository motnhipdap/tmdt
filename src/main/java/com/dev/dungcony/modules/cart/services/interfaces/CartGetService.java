package com.dev.dungcony.modules.cart.services.interfaces;

import com.dev.dungcony.modules.cart.dtos.CartDto;

import java.util.UUID;

public interface CartGetService {

    CartDto getCart(UUID userId);
}
