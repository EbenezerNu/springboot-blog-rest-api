package com.springboot.blog.payload;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class LoginDto {

    private PasswordEncoder passwordEncoder;

    private String usernameOrEmail;
    private String password;

    /*public String getPassword() {
        return passwordEncoder.encode(password);
    }*/
}
