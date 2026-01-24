package com.dev.dungcony.modules.authorization.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegisReq {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private final String email;
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải có ít nhất 3 ký tự và tối đa 50 ký tự")
    private final String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, max = 50, message = "Mật khẩu phải có ít nhất 8 ký tự và tối đa 50 ký tự")
    private final String password;
}
