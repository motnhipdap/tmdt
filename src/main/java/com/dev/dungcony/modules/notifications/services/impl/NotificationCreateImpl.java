package com.dev.dungcony.modules.notifications.services.impl;

import com.dev.dungcony.modules.notifications.dtos.req.AdminCreateNotificationReq;
import com.dev.dungcony.modules.notifications.dtos.req.UserNotificationCreateReq;
import com.dev.dungcony.modules.notifications.entities.Notification;
import com.dev.dungcony.modules.notifications.enums.NotificationType;
import com.dev.dungcony.modules.notifications.exceptions.NotiUnAuthException;
import com.dev.dungcony.modules.notifications.mappers.NotiMapper;
import com.dev.dungcony.modules.notifications.repositories.NotificationRepository;
import com.dev.dungcony.modules.notifications.services.interfaces.NotificationCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationCreateImpl implements NotificationCreateService {

    private final NotificationRepository notificationRepository;

    @Override
    public String userCreate(UUID senderId, UserNotificationCreateReq req) {
        if (!senderId.equals(req.senderId()))
            throw new NotiUnAuthException();

        Notification noti = NotiMapper.toEntity(req);

        notificationRepository.save(noti);

        return noti.getCode();
    }

    @Override
    public void userCreateOrder(UUID uid) {
        Notification noti = new Notification();
        noti.setSenderId(uid);
        noti.setType(NotificationType.ORDER_CREATED);
        noti.setTitle("create_order");
        noti.setMessage("tạo đơn hàng");

        notificationRepository.save(noti);
    }


    @Override
    public void userPailOrder(UUID uid) {
        Notification noti = new Notification();
        noti.setSenderId(uid);
        noti.setType(NotificationType.ORDER_PAID);
        noti.setTitle("paid_order");
        noti.setMessage("thanh toasn đơn hàng thành công");

        notificationRepository.save(noti);
    }

    @Override
    public void userCancelOrder(UUID uid) {
        Notification noti = new Notification();
        noti.setSenderId(uid);
        noti.setType(NotificationType.ORDER_CANCELLED);
        noti.setTitle("cancel_order");
        noti.setMessage("hủy đơn hàng");

        notificationRepository.save(noti);
    }

    @Transactional
    @Override
    public List<String> adminCreate(AdminCreateNotificationReq req) {

        List<Notification> notis = NotiMapper.toEntity(req);

        if (notis.isEmpty()) {
            return Collections.emptyList();
        }

        List<Notification> savedNotis = notificationRepository.saveAll(notis);

        return savedNotis.stream()
                .map(Notification::getCode)
                .filter(Objects::nonNull)
                .toList();
    }
}
