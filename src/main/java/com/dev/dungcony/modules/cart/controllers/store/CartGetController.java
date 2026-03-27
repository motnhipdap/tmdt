package com.dev.dungcony.modules.cart.controllers.store;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.cart.dtos.res.CartRes;
import com.dev.dungcony.modules.cart.services.interfaces.CartGetService;
import com.dev.dungcony.modules.users.entities.User;
import com.dev.dungcony.modules.users.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/user/cart")
@Tag(name = "Cart")
public class CartGetController {

    private final CartGetService cartGetService;

    @Operation(summary = "Lấy giỏ hàng")
    @GetMapping
    public ResponseEntity<ApiRes<CartRes>> getCart(
            @AuthenticationPrincipal AccountDetails account,
            @RequestParam("user_id") UUID userId) {
        return ResponseEntity.ok(
                ApiRes.success("Cart retrieved", cartGetService.getCart(userId)));
    }
}
