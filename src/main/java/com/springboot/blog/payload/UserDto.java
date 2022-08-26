package com.springboot.blog.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    private Long id;

    @NotNull
    @Size(min = 3, message = "User's username should be at least 3 characters")
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, message = "User's password should be at least 8 characters")
    private String password;
}
