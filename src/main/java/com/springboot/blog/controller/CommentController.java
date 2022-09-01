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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private CommentService commentService;

    private PaginationUtil<Comment, CommentDto> paginationUtil;

    public CommentController(CommentService commentService, PaginationUtil<Comment, CommentDto> paginationUtil) {
        this.commentService = commentService;
        this.paginationUtil = paginationUtil;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/all")
    public ResponseEntity<Pagination<CommentDto>> fetchAllComments(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        log.info("Inside fetchPosts -->");
        Params params = paginationUtil.fetchParams(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(commentService.getAllComments(params), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping
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


    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<CommentDto> saveComment(@PathVariable(name = "postId") long postId, @Valid @RequestBody CommentDto commentDto){
        log.info("Inside saveComment -->");
        return new ResponseEntity<>(commentService.saveComment(postId, commentDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "postId") long postId, @PathVariable(name = "id") long id){
        log.info("Inside deleteComment -->");
        commentService.deleteComment(postId, id);
        return new ResponseEntity<>("Successfully deleted comment of 'id' "+ id + " in Post of id " + postId, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(name = "postId") long postId, @PathVariable(name = "id") long id, @RequestBody CommentDto commentDto){
        log.info("Inside updateComment -->");
        commentDto.setId(id);
        return new ResponseEntity<>(commentService.updateComment(postId, commentDto), HttpStatus.CREATED);
    }



}
