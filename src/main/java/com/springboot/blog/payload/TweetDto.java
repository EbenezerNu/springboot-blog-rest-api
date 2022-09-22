package com.springboot.blog.payload;

import com.springboot.blog.entity.Comment;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class TweetDto {

    private Long id;

    @NotNull
    @Size(min = 2, message = "Title should have at least 2 characters")
    private String title;

    @NotNull
    @Size(min = 10, message = "Description should have at least 10 characters")
    private String description;

    @NotEmpty(message = "Content should not be null or empty")
    private String content;

    private List<CommentDto> comments = new ArrayList<>();

    private List<PostDto> posts = new ArrayList<>();
}