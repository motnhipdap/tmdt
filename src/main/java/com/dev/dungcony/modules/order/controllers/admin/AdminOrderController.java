package com.dev.dungcony.modules.order.controllers.admin;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.dungcony.commons.dtos.ApiRes;
import com.dev.dungcony.commons.dtos.PageRes;
import com.dev.dungcony.modules.order.dtos.req.UpdateOrderStatusReq;
import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.dtos.res.OrderSummaryRes;
import com.dev.dungcony.modules.order.enums.OrderStatus;
import com.dev.dungcony.modules.order.services.interfaces.OrderCommandService;
import com.dev.dungcony.modules.order.services.interfaces.OrderGetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/admin/order")
@Tag(name = "Orders (Admin)")
public class AdminOrderController {

    private final OrderCommandService orderCommandService;
    private final OrderGetService orderGetService;

    @Operation(summary = "Lấy tất cả đơn hàng", description = "Phân trang, hỗ trợ sort")
    @GetMapping("/get-all")
    public ResponseEntity<ApiRes<PageRes<OrderSummaryRes>>> getAllOrders(
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(
                ApiRes.success("All orders",
                        PageRes.from(orderGetService.getAllOrders(pageable))));
    }

    @Operation(summary = "Lấy đơn hàng theo trạng thái")
    @GetMapping("/get-by-status")
    public ResponseEntity<ApiRes<PageRes<OrderSummaryRes>>> getOrdersByStatus(
            @Parameter(description = "Trạng thái đơn hàng") @RequestParam("status") OrderStatus status,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(
                ApiRes.success("Orders by status",
                        PageRes.from(orderGetService.getAllOrdersByStatus(status, pageable))));
    }

    @Operation(summary = "Xem chi tiết đơn hàng")
    @GetMapping("/{orderCode}")
    public ResponseEntity<ApiRes<OrderRes>> getOrderDetail(
            @PathVariable String orderCode) {
        return ResponseEntity.ok(
                ApiRes.success("Order detail",
                        orderGetService.getOrderByCodeAdmin(orderCode)));
    }

    @Operation(summary = "Cập nhật trạng thái đơn hàng")
    @PatchMapping("/update-status")
    public ResponseEntity<ApiRes<Void>> updateStatus(
            @Valid @RequestBody UpdateOrderStatusReq req) {
        orderCommandService.updateOrderStatus(req.orderCode(), req.status());
        return ResponseEntity.ok(ApiRes.success("Order status updated to " + req.status()));
    }
}
