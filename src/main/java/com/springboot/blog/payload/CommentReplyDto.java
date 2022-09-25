package com.springboot.blog.payload;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CommentReplyDto {

    private Long id;
    
    @NotEmpty(message = "Email should not be null or empty")
    @Email
    private String email;
    
    @NotEmpty(message = "Name should not be null or empty")
    @Size(min = 3, message = "Name should have at least 3 characters")
    private String name;

    @NotNull
    @Size(min = 2, message = "Comment content should have at least 2 characters")
    private String body;

    private Long commentId;
}
