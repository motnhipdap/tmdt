package com.dev.dungcony.modules.users.services.interfaces;

import com.dev.dungcony.modules.users.dtos.ReceiverDto;
import com.dev.dungcony.modules.users.dtos.res.ReceiverRes;

import java.util.List;
import java.util.UUID;

public interface RecieverGetService {
    ReceiverDto getById(UUID userId, Integer id);

    ReceiverRes getReceiverById(UUID userId, Integer id);

    List<ReceiverRes> getAllByUser(UUID userId);
}
