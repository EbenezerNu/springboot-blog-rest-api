package com.springboot.blog.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    private Long id;
    @NotNull
    private String username;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
}
