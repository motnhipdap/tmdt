package com.dev.dungcony.modules.cart.controllers.store;

import java.util.UUID;

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
import com.dev.dungcony.modules.cart.services.interfaces.CartItemUpdateService;
import com.dev.dungcony.modules.cart.services.interfaces.CartGetService;
import com.dev.dungcony.modules.cart.services.interfaces.CartRemoveService;

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
public class CartController {

    private final CartRemoveService cartItemService;
    private final CartGetService cartGetService;
    private final CartItemUpdateService cartCreateService;

    @Operation(summary = "Tạo giỏ hàng")
    @PostMapping("/create")
    public ResponseEntity<ApiRes<Void>> createCart(
            @AuthenticationPrincipal AccountDetails account,
            @RequestParam("userId") UUID userId) {
        cartCreateService.createCart(userId);
        return ResponseEntity.ok(ApiRes.success("Cart created"));
    }

    @Operation(summary = "Lấy giỏ hàng")
    @GetMapping
    public ResponseEntity<ApiRes<CartRes>> getCart(
            @AuthenticationPrincipal AccountDetails account,
            @RequestParam("cartId") Integer cartId) {
        return ResponseEntity.ok(
                ApiRes.success("Cart retrieved", cartGetService.getCart(cartId)));
    }

    @Operation(summary = "Thêm sản phẩm vào giỏ hàng")
    @PostMapping("/add")
    public ResponseEntity<ApiRes<Void>> addToCart(
            @AuthenticationPrincipal AccountDetails account,
            @Valid @RequestBody AddToCartReq req) {
        cartItemService.addItemToCart(req);
        return ResponseEntity.ok(ApiRes.success("Item added to cart"));
    }

    @Operation(summary = "Cập nhật số lượng sản phẩm")
    @PatchMapping("/update-quantity")
    public ResponseEntity<ApiRes<Void>> updateQuantity(
            @AuthenticationPrincipal AccountDetails account,
            @Valid @RequestBody UpdateCartItemReq req) {
        cartItemService.updateItemQuantity(req);
        return ResponseEntity.ok(ApiRes.success("Quantity updated"));
    }

    @Operation(summary = "Xóa sản phẩm khỏi giỏ hàng")
    @DeleteMapping("/remove")
    public ResponseEntity<ApiRes<Void>> removeItem(
            @AuthenticationPrincipal AccountDetails account,
            @RequestParam("cartId") Integer cartId,
            @RequestParam("productCode") String productCode,
            @RequestParam("sizeId") Integer sizeId) {
        cartItemService.removeItemFromCart(cartId, productCode, sizeId);
        return ResponseEntity.ok(ApiRes.success("Item removed from cart"));
    }

    // @Operation(summary = "Chọn/bỏ chọn một sản phẩm")
    // @PatchMapping("/toggle-select")
    // public ResponseEntity<ApiRes<Void>> toggleSelect(
    // @AuthenticationPrincipal AccountDetails account,
    // @RequestParam("productCode") String productCode,
    // @RequestParam("sizeId") Integer sizeId) {
    // cartItemService.toggleItemSelection(getUserId(account), productCode, sizeId);
    // return ResponseEntity.ok(ApiRes.success("Selection toggled"));
    // }

    // @Operation(summary = "Chọn/bỏ chọn tất cả sản phẩm")
    // @PatchMapping("/toggle-select-all")
    // public ResponseEntity<ApiRes<Void>> toggleSelectAll(
    // @AuthenticationPrincipal AccountDetails account,
    // @RequestParam("selected") boolean selected) {
    // cartItemService.toggleSelectAll(getUserId(account), selected);
    // return ResponseEntity.ok(ApiRes.success("All items " + (selected ? "selected"
    // : "deselected")));
    // }

    @Operation(summary = "Xóa toàn bộ giỏ hàng")
    @DeleteMapping("/clear")
    public ResponseEntity<ApiRes<Void>> clearCart(
            @AuthenticationPrincipal AccountDetails account,
            @RequestParam("cartId") Integer cartId) {
        cartItemService.clearCart(cartId);
        return ResponseEntity.ok(ApiRes.success("Cart cleared"));
    }
}
