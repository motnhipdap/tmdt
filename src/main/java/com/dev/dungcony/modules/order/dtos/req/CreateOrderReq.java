package com.dev.dungcony.modules.order.dtos.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrderReq(
        @NotBlank @Size(max = 255) String shippingAddress,
        @NotBlank @Size(max = 15) String phone,
        @NotBlank @Size(max = 100) String receiverName,
        @Size(max = 500) String note) {
}
