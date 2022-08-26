package com.springboot.blog.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class PostDto {

    private Long id;

    @NotNull
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;

    @NotNull
    @Size(min = 10, message = "Post description should have at least 10 characters")
    private String description;

    @NotNull
    private String content;

    private Set<PostCommentDto> comments;
}
