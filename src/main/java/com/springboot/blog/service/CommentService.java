package com.springboot.blog.service;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.PostCommentDto;
import com.springboot.blog.utils.Pagination;
import com.springboot.blog.utils.Params;

import java.util.Set;

public interface CommentService {

    Pagination<CommentDto> getAllComments(Params params);

    CommentDto saveComment(Long postId, CommentDto commentDto);

    Pagination<CommentDto> getPostComments(long postId, Params params);

    Set<PostCommentDto> getPostComments(long postId);

    void deleteComment(long postId, long id);

    CommentDto updateComment(long postId, CommentDto commentDto);
}
