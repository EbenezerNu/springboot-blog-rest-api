package com.springboot.blog.service;

import com.springboot.blog.payload.NestedPostDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.utils.Pagination;
import com.springboot.blog.utils.Params;

import java.util.List;


public interface PostService {

    PostDto createPost(PostDto postDto);

    Pagination<PostDto> getAllPosts(Params params);

    PostDto getPostById(Long id);

    PostDto updatePost(Long id, PostDto postDto);

    void deletePost(Long id);

    NestedPostDto getPostByIdAsTweet(Long id);
}
