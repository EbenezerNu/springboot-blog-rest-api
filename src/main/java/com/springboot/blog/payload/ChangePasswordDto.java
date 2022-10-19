package com.springboot.blog.payload;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordDto {

    @Nullable
    private String username;

    @Nullable
    private String email;

    @Nullable
    private String oldPassword;

    @Nullable
    private String newPassword;

}
