package com.springboot.blog.controller;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.NestedPostDto;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api")
public class PostController {

    private PostService postService;

    private PaginationUtil<Post, PostDto> paginationUtil;

    public PostController(PostService postService, PaginationUtil<Post, PostDto> paginationUtil) {
        this.postService = postService;
        this.paginationUtil = paginationUtil;
    }

    // create post api
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/v1/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        log.info("Inside createPost -->");
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    // fetching posts api
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/v1/posts")
    public ResponseEntity<Pagination<PostDto>> fetchPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        log.info("Inside fetchPosts -->");
        Params params = paginationUtil.fetchParams(pageNo, pageSize, sortBy, sortDir, search);
        return new ResponseEntity<>(postService.getAllPosts(params), HttpStatus.OK);
    }

    // fetching post by id api
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/v1/posts/{id}")
    public ResponseEntity<PostDto> fetchPost(@PathVariable(name = "id") Long id){
        log.info("Inside fetchPost by id--> {}", id);
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    // fetching post by id api
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/v2/posts/{id}")
    public ResponseEntity<NestedPostDto> fetchPostTweet(@PathVariable(name = "id") Long id){
        log.info("Inside fetchPostTweet by id--> {}", id);
        return new ResponseEntity<>(postService.getPostByIdAsTweet(id), HttpStatus.OK);
    }

    // update a post by id api
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/v1/posts/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable(name = "id") Long id){
        log.info("Inside updatePost by id -->", id);
        return new ResponseEntity<>(postService.updatePost(id, postDto), HttpStatus.OK);
    }

    // delete a post by id api
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/v1/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id){
        log.info("Inside deletePost by id -->", id);
        postService.deletePost(id);
        return new ResponseEntity<>("Post of Id '" + id + "' successfully deleted!", HttpStatus.OK);
    }



}
