package com.springboot.blog.controller;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
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
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy){
        log.info("Inside fetchPosts -->");
        return new ResponseEntity<>(postService.getAllPosts(pageNo, pageSize, sortBy), HttpStatus.OK);
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
