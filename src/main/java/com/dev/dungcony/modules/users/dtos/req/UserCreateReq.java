package com.dev.dungcony.modules.users.dtos.req;

public record UserCreateReq(
                String firstName,
                String lastName,
                String avatar) {
        public UserCreateReq() {
                this(null, null, null);
        }

}
