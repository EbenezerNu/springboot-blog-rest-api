package com.springboot.blog.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SignUpDto {

    @NotNull(message = "User's name should not be null")
    @Size(min = 3, message = "User's name should be at least 3 characters")
    private String name;

    @NotNull(message = "User's username should not be null")
    @Size(min = 3, message = "User's username should be at least 3 characters")
    private String username;

    @NotNull(message = "User's email should not be null")
    @Email
    private String email;

    @NotNull
    @Size(min = 8, message = "User's password should be at least 8 characters")
    private String password;;

}
