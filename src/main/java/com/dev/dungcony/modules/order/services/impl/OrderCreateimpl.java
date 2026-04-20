package com.dev.dungcony.modules.order.services.impl;

import com.dev.dungcony.modules.cart.services.interfaces.CartItemGetService;
import com.dev.dungcony.modules.order.dtos.OrderItemDto;
import com.dev.dungcony.modules.order.dtos.req.CreateOrderReq;
import com.dev.dungcony.modules.order.dtos.res.OrderRes;
import com.dev.dungcony.modules.order.entities.Order;
import com.dev.dungcony.modules.order.entities.OrderItem;
import com.dev.dungcony.modules.order.entities.OrderItemId;
import com.dev.dungcony.modules.order.enums.OrderStatus;
import com.dev.dungcony.modules.order.enums.PaymentType;
import com.dev.dungcony.modules.order.exceptions.OrderCannotCreateException;
import com.dev.dungcony.modules.order.exceptions.OrderConflictException;
import com.dev.dungcony.modules.order.mappers.OrderMapper;
import com.dev.dungcony.modules.order.repositories.OrderRepository;
import com.dev.dungcony.modules.order.services.interfaces.OrderCreateService;
import com.dev.dungcony.modules.payment.dtos.res.PaymentRes;
import com.dev.dungcony.modules.payment.services.impl.VnPayImpl;
import com.dev.dungcony.modules.product.dtos.ProductDto;
import com.dev.dungcony.modules.product.services.interfaces.SizeCacheService;
import com.dev.dungcony.modules.product.services.interfaces.product.ProductGetService;
import com.dev.dungcony.modules.users.dtos.res.ReceiverRes;
import com.dev.dungcony.modules.users.services.interfaces.RecieverGetService;
import com.dev.dungcony.modules.voucher.services.interfaces.UserVoucherGetService;
import com.dev.dungcony.modules.voucher.services.interfaces.UserVoucherUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreateimpl implements OrderCreateService {

    private final OrderRepository orderRepo;

    private final ProductGetService productGetService;
    private final SizeCacheService sizeCacheService;
    private final RecieverGetService recieverGetService;
    private final UserVoucherGetService userVoucherService;
    private final UserVoucherUpdateService userVoucherUpdateService;
    private final CartItemGetService cartItemGetService;
    private final VnPayImpl vnPayImpl;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public OrderRes createOrder(UUID userId, CreateOrderReq req, String ipAddress) {

        if (req.items() == null || req.items().isEmpty())
            throw new OrderCannotCreateException();

        ReceiverRes receiver = recieverGetService.getReceiverById(userId, req.recieverid());

        // ----------- set Order -----------//
        Order order = new Order();
        order.setUserId(userId);
        order.setReceiverId(req.recieverid());
        order.setCode(generateOrderCode());
        order.setPaymentType(req.paymentType());
        if (req.paymentType() == PaymentType.ONLINE) {
            order.setStatus(OrderStatus.UNPAID);
        } else {
            order.setStatus(OrderStatus.PENDING);
        }
        order.setNote(req.note());

        // ----------- validate ---------/
        List<String> productCodes = req.items().stream()
                .map(OrderItemDto::productCode)
                .toList();

        if (!cartItemGetService.existsLitProductCode(userId, productCodes)) {
            throw new OrderCannotCreateException("CONFLICT_PRODUCT_CODE", "prouduct phải thuộc giỏ hàng");
        }

        // lấy thông tin chuẩn của từng sản phẩm
        Map<String, ProductDto> products = productGetService.getDtoByCodes(productCodes);

        // tạo chi tiết đơn hanàng
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItemDto> savedItems = new ArrayList<>();

        for (OrderItemDto itemDto : req.items()) {
            if (itemDto.quantity() <= 0)
                throw new OrderCannotCreateException(
                        "ITEM_QUANTITY_IS_ZERO",
                        "số lượng sản phẩm phải > 0");

            int sizeId = sizeCacheService.getIdBySize(itemDto.size());
            ProductDto productDto = products.get(itemDto.productCode());

            OrderItem orderItem = getOrderItem(itemDto, productDto, sizeId);

            order.addItem(orderItem);

            totalPrice = totalPrice.add(orderItem.getTotalPrice());

            savedItems.add(new OrderItemDto(
                    itemDto.productCode(),
                    itemDto.size(),
                    orderItem.getQuantity(),
                    orderItem.getOriginalPrice(),
                    orderItem.getFinalPrice()));
        }

        BigDecimal finalPrice = userVoucherService.applyVoucher(req.voucherCode(), userId, totalPrice);

        BigDecimal voucherDiscount = totalPrice.subtract(finalPrice);

        validateClientTotals(req, totalPrice, voucherDiscount, finalPrice);

        order.setTotalPrice(totalPrice);
        order.setVoucherCode(req.voucherCode());
        order.setVoucherDiscount(voucherDiscount);
        order.setFinalPrice(finalPrice);
        orderRepo.save(order);
        userVoucherUpdateService.apllyVoucherComplete(userId, req.voucherCode());

        log.info("Order created: {} for user: {}", order.getCode(), userId);
        notificationService.onOrderCreated(order.getCode(), finalPrice);

        String paymentUrl = null;
        if (req.paymentType() == PaymentType.ONLINE) {
            PaymentRes paymentRes = vnPayImpl.createPaymentUrl(userId, order.getCode(), ipAddress);
            paymentUrl = paymentRes.paymentUrl();
        }

        return OrderMapper.toOrderRes(order, savedItems, receiver, paymentUrl);
    }

    // -----------------------------PRIVATE-----------------------------------//
    private String generateOrderCode() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "ORD-" + timestamp.substring(timestamp.length() - 8) + "-" + random;
    }

    private @NonNull OrderItem getOrderItem(OrderItemDto itemDto, ProductDto product, int sizeId) {
        if (itemDto.finalPrice() == null || itemDto.originalPrice() == null) {
            throw new OrderCannotCreateException("PRICE_IS_REQUIRE", "giá sản phẩm phải có");
        }

        if (itemDto.originalPrice().compareTo(product.originalPrice()) != 0 ||
                itemDto.finalPrice().compareTo(product.finalPrice()) != 0) {
            throw new OrderCannotCreateException(
                    "INPUT_ERROR",
                    "thông tin sản phẩm không đúng");
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setId(new OrderItemId(null, product.id(), sizeId));
        orderItem.setQuantity(itemDto.quantity());
        orderItem.setOriginalPrice(product.originalPrice());
        orderItem.setFinalPrice(product.finalPrice());
        orderItem.setTotalPrice(product.finalPrice().multiply(BigDecimal.valueOf(itemDto.quantity())));
        return orderItem;
    }

    // kiểm tra lại dữ liệu với dữ liệu client gửi lên
    private void validateClientTotals(
            CreateOrderReq req,
            BigDecimal subtotalAmount,
            BigDecimal voucherDiscount,
            BigDecimal finalAmount) {
        if (req.totalPrice() != null && req.totalPrice().compareTo(subtotalAmount) != 0)
            throw new OrderConflictException("Order total price has changed");

        if (req.voucherDiscount() != null && req.voucherDiscount().compareTo(voucherDiscount) != 0)
            throw new OrderConflictException("Voucher discount has changed");

        if (req.finalPrice() != null && req.finalPrice().compareTo(finalAmount) != 0)
            throw new OrderConflictException("Order final price has changed");

    }
}
