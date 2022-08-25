package com.springboot.blog.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PostCommentDto {

    private Long id;
    @NotNull
    @Size(min = 2, message = "Comment content should have at least 2 characters")
    private String content;
    @NotNull
    private Long userId;
}
