package com.springboot.blog.payload;

import lombok.Data;

@Data
public class CommentDto {

    private Long id;
    private String content;
    private Long postId;
    private Long userId;
}
