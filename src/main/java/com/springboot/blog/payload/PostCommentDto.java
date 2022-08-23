package com.springboot.blog.payload;

import lombok.Data;

@Data
public class PostCommentDto {

    private Long id;
    private String content;
    private Long userId;
}
