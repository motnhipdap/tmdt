package com.dev.dungcony.modules.notifications.dtos.req;

import com.dev.dungcony.modules.notifications.enums.NotificationType;

import java.util.List;
import java.util.UUID;

public record AdminCreateNotificationReq(
        List<UUID> receivers,
        String title,
        String message
) {
}
