package com.springboot.blog.controller;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.utils.AppConstants;
import com.springboot.blog.utils.Pagination;
import com.springboot.blog.utils.PaginationUtil;
import com.springboot.blog.utils.Params;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
//@RequestMapping("/api/posts/{id}/comments")
@RequestMapping("/api/comments")
public class CommentController {

    private CommentService commentService;

    private PaginationUtil<Comment, CommentDto> paginationUtil;

    public CommentController(CommentService commentService, PaginationUtil<Comment, CommentDto> paginationUtil) {
        this.commentService = commentService;
        this.paginationUtil = paginationUtil;
    }

    @GetMapping
    public ResponseEntity<Pagination<CommentDto>> fetchAllComments(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        log.info("Inside fetchPosts -->");
        Params params = paginationUtil.fetchParams(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(commentService.getAllComments(params), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Pagination<CommentDto>> fetchPostComments(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @PathVariable(name = "postId") long postId) {
        log.info("Inside fetchPostComments -->");
        Params params = paginationUtil.fetchParams(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(commentService.getPostComments(postId, params), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<CommentDto> saveComment(@RequestBody CommentDto commentDto){
        log.info("Inside saveComment -->");
        return new ResponseEntity<>(commentService.saveComment(commentDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "id") long id){
        log.info("Inside deleteComment -->");
        commentService.deleteComment(id);
        return new ResponseEntity<>("Successfully deleted comment of 'id' "+ id, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(name = "id") long id, @RequestBody CommentDto commentDto){
        log.info("Inside updateComment -->");
        return new ResponseEntity<>(commentService.updateComment(id, commentDto), HttpStatus.CREATED);
    }



}
