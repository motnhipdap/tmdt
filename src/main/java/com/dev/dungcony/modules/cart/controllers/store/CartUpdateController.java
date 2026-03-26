package com.dev.dungcony.modules.cart.controllers.store;

import java.util.UUID;

import com.dev.dungcony.modules.cart.services.interfaces.CartUpdateService;
import com.dev.dungcony.modules.product.enums.ProductSize;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.modules.cart.dtos.req.AddToCartReq;
import com.dev.dungcony.modules.cart.dtos.req.UpdateCartItemReq;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/user/cart")
@Tag(name = "Cart")
public class CartUpdateController {

    private final CartUpdateService cartUpdateService;

    @Operation(summary = "Thêm sản phẩm vào giỏ hàng")
    @PostMapping("/add")
    public ResponseEntity<ApiRes<Void>> addToCart(
            @AuthenticationPrincipal AccountDetails account,
            @Valid @RequestBody AddToCartReq req) {
        cartUpdateService.addItemToCart(req);
        return ResponseEntity.ok(ApiRes.success("Item added to cart"));
    }

    @Operation(summary = "Cập nhật số lượng sản phẩm")
    @PatchMapping("/update-quantity")
    public ResponseEntity<ApiRes<Void>> updateQuantity(
            @AuthenticationPrincipal AccountDetails account,
            @Valid @RequestBody UpdateCartItemReq req) {
        cartUpdateService.updateItemQuantity(req);
        return ResponseEntity.ok(ApiRes.success("Quantity updated"));
    }

    @Operation(summary = "Xóa sản phẩm khỏi giỏ hàng")
    @DeleteMapping("/remove")
    public ResponseEntity<ApiRes<Void>> removeItem(
            @AuthenticationPrincipal AccountDetails account,
            @RequestParam("userId") UUID userId,
            @RequestParam("productCode") String productCode,
            @RequestParam("size") ProductSize size) {
        cartUpdateService.removeItemFromCart(userId, productCode, size);
        return ResponseEntity.ok(ApiRes.success("Item removed from cart"));
    }

    @Operation(summary = "Xóa toàn bộ giỏ hàng")
    @DeleteMapping("/clear")
    public ResponseEntity<ApiRes<Void>> clearCart(
            @AuthenticationPrincipal AccountDetails account,
            @RequestParam("userId") UUID userId) {
        cartUpdateService.clearCart(userId);
        return ResponseEntity.ok(ApiRes.success("Cart cleared"));
    }
}
