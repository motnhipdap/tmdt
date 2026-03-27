package com.dev.dungcony.modules.order.services.interfaces;

import java.util.UUID;

public interface OrderUpdateService {
    void cancelOrder(UUID userId, String orderCode);

    void completedOrder(UUID userId, String orderCode);
}
