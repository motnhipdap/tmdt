package com.dev.dungcony.modules.users.services.impl;

import com.dev.dungcony.modules.users.dtos.LoginReq;
import com.dev.dungcony.modules.users.dtos.LoginRes;
import com.dev.dungcony.modules.users.dtos.UserDTO;
import com.dev.dungcony.modules.users.services.interfaces.UserServiceInterface;
import com.dev.dungcony.services.BaseService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService implements UserServiceInterface {

    @Override
    public LoginRes login(LoginReq loginReq) {
        try {

            String email = loginReq.getEmail();
            String password = loginReq.getPassword();

            if (email != null && password != null) {
                String token = "RandomToken";
                UserDTO userDTO = new UserDTO(1L, "John Doe", email);

                return new LoginRes(token, userDTO);
            } else {

            }

        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException("có vấn đề xảy ra");
        }
        return null;
    }

}
