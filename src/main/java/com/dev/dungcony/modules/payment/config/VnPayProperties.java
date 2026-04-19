package com.dev.dungcony.modules.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vnpay")
public record VnPayProperties(
        String tmnCode,
        String hashSecret,
        String payUrl,
        String returnUrl,
        String version,
        String command,
        String orderType
) {
    public VnPayProperties {
        if (version == null) version = "2.1.0";
        if (command == null) command = "pay";
        if (orderType == null) orderType = "other";
    }
}
