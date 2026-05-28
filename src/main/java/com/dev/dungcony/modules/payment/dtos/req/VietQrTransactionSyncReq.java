package com.dev.dungcony.modules.payment.dtos.req;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;

public record VietQrTransactionSyncReq(
        String bankaccount,
        BigDecimal amount,
        String transType,
        String content,
        String transactionid,
        Long transactiontime,
        String referencenumber,
        @JsonAlias({"orderId", "orderid", "order_id", "order_code"})
        String orderCode,
        String terminalCode,
        String subTerminalCode,
        String serviceCode,
        String urlLink,
        String sign
) {
}
