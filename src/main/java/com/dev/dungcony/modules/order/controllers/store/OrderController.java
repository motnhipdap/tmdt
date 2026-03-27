package com.dev.dungcony.modules.order.controllers.store;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dungcony.commons.dtos.AccountDetails;
import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.commons.dtos.PageRes;
import com.dev.dungcony.modules.order.dtos.req.CreateOrderReq;
import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.dtos.res.OrderSummaryRes;
import com.dev.dungcony.modules.order.enums.OrderStatus;
import com.dev.dungcony.modules.order.services.interfaces.OrderGetService;
import com.dev.dungcony.modules.users.entities.User;
import com.dev.dungcony.modules.users.repositories.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/user/order")
@Tag(name = "Orders")
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderGetService orderGetService;
    private final UserRepository userRepository;

    @Operation(summary = "Tạo đơn hàng từ giỏ hàng", description = "Tạo đơn hàng từ các sản phẩm đã chọn trong giỏ")
    @PostMapping("/create")
    public ResponseEntity<ApiRes<OrderRes>> createOrder(
            @AuthenticationPrincipal AccountDetails account,
            @Valid @RequestBody CreateOrderReq req) {
        OrderRes order = orderCommandService.createOrderFromCart(getUserId(account), req);
        return ResponseEntity.ok(ApiRes.success("Order created successfully", order));
    }

    @Operation(summary = "Lấy danh sách đơn hàng")
    @GetMapping("/my-orders")
    public ResponseEntity<ApiRes<PageRes<OrderSummaryRes>>> getMyOrders(
            @AuthenticationPrincipal AccountDetails account,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(
                ApiRes.success("Orders retrieved",
                        PageRes.from(orderGetService.getUserOrders(getUserId(account), pageable))));
    }

    @Operation(summary = "Lấy danh sách đơn hàng theo trạng thái")
    @GetMapping("/my-orders/status")
    public ResponseEntity<ApiRes<PageRes<OrderSummaryRes>>> getMyOrdersByStatus(
            @AuthenticationPrincipal AccountDetails account,
            @Parameter(description = "Trạng thái đơn hàng") @RequestParam("status") OrderStatus status,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(
                ApiRes.success("Orders retrieved",
                        PageRes.from(orderGetService.getUserOrdersByStatus(getUserId(account), status, pageable))));
    }

    @Operation(summary = "Xem chi tiết đơn hàng")
    @GetMapping("/{orderCode}")
    public ResponseEntity<ApiRes<OrderRes>> getOrderDetail(
            @AuthenticationPrincipal AccountDetails account,
            @PathVariable String orderCode) {
        return ResponseEntity.ok(
                ApiRes.success("Order detail",
                        orderGetService.getOrderByCode(getUserId(account), orderCode)));
    }

    @Operation(summary = "Hủy đơn hàng", description = "Chỉ hủy được đơn hàng ở trạng thái PENDING")
    @PatchMapping("/{orderCode}/cancel")
    public ResponseEntity<ApiRes<Void>> cancelOrder(
            @AuthenticationPrincipal AccountDetails account,
            @PathVariable String orderCode) {
        orderCommandService.cancelOrder(getUserId(account), orderCode);
        return ResponseEntity.ok(ApiRes.success("Order cancelled"));
    }

    private java.util.UUID getUserId(AccountDetails account) {
        User user = userRepository.findByAccountId(account.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
