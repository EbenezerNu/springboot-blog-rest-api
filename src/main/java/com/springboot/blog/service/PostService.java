package com.springboot.blog.service;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.utils.Pagination;

import java.util.List;


public interface PostService {

    PostDto createPost(PostDto postDto);

    Pagination<PostDto> getAllPosts(int pageNo, int pageSize, String sortBy);

    PostDto getPostById(Long id);

    PostDto updatePost(Long id, PostDto postDto);

    void deletePost(Long id);
}
