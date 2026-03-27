package com.dev.dungcony.modules.users.services.impl;

import com.dev.dungcony.modules.users.dtos.req.ReceiverCreateReq;
import com.dev.dungcony.modules.users.dtos.res.ReceiverRes;
import com.dev.dungcony.modules.users.entities.Address;
import com.dev.dungcony.modules.users.entities.Receiver;
import com.dev.dungcony.modules.users.entities.User;
import com.dev.dungcony.modules.users.mappers.AddressMapper;
import com.dev.dungcony.modules.users.mappers.ReceiverMapper;
import com.dev.dungcony.modules.users.repositories.RecieverRepository;
import com.dev.dungcony.modules.users.services.interfaces.ReceiverCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReceiverCreateImpl implements ReceiverCreateService {

    private final RecieverRepository recieverRepository;

    @Override
    public ReceiverRes create(UUID userId, ReceiverCreateReq req) {

        Address address = AddressMapper.toEntity(req.addr());
        User user = new User();
        user.setId(userId);

        Receiver receiver = ReceiverMapper.toEntity(req);
        receiver.setUser(user);

        recieverRepository.save(receiver);

        return ReceiverMapper.toRes(receiver);
    }
}
