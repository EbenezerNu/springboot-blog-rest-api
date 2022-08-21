package com.springboot.blog.controller;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import com.springboot.blog.utils.Pagination;
import com.springboot.blog.utils.PaginationUtil;
import com.springboot.blog.utils.Params;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    private PaginationUtil<Post, PostDto> paginationUtil;

    public PostController(PostService postService, PaginationUtil<Post, PostDto> paginationUtil) {
        this.postService = postService;
        this.paginationUtil = paginationUtil;
    }

    // create post api
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto){
        log.info("Inside createPost -->");
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    // fetching posts api
    @GetMapping
    public ResponseEntity<Pagination<PostDto>> fetchPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        log.info("Inside fetchPosts -->");
        Params params = paginationUtil.fetchParams(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postService.getAllPosts(params), HttpStatus.OK);
    }

    // fetching post by id api
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> fetchPost(@PathVariable(name = "id") Long id){
        log.info("Inside fetchPost by id-->");
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    // update a post by id api
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable(name = "id") Long id){
        log.info("Inside updatePost by id -->", id);
        return new ResponseEntity<>(postService.updatePost(id, postDto), HttpStatus.OK);
    }

    // delete a post by id api
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id){
        log.info("Inside deletePost by id -->", id);
        postService.deletePost(id);
        return new ResponseEntity<>("Post of Id '" + id + "' successfully deleted!", HttpStatus.OK);
    }



}
