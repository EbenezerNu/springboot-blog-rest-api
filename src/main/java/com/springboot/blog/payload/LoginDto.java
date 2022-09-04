package com.springboot.blog.payload;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;

@Data
public class LoginDto {

    private PasswordEncoder passwordEncoder;

    @NotNull(message = "Username or Email cannot be null")
    private String usernameOrEmail;

    @NotNull(message = "Password cannot be null")
    private String password;

    /*public String getPassword() {
        return passwordEncoder.encode(password);
    }*/
}
