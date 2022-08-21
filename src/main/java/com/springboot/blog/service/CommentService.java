package com.springboot.blog.service;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.utils.Pagination;
import com.springboot.blog.utils.Params;

public interface CommentService {

    Pagination<CommentDto> getAllComments(Params params);

    CommentDto saveComment(CommentDto commentDto);

    Pagination<CommentDto> getPostComments(long postId, Params params);

    void deleteComment(long id);

    CommentDto updateComment(long id, CommentDto commentDto);
}
