package com.dev.dungcony.modules.notifications.services.interfaces;

import com.dev.dungcony.modules.notifications.dtos.req.AdminCreateNotificationReq;
import com.dev.dungcony.modules.notifications.dtos.req.UserNotificationCreateReq;

import java.util.List;
import java.util.UUID;

public interface NotificationCreateService {
    String userCreate(UUID senderId, UserNotificationCreateReq req);

    List<String> adminCreate(AdminCreateNotificationReq req);
}
