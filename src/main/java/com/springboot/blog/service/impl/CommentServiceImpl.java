package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFound;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.utils.Pagination;
import com.springboot.blog.utils.PaginationUtil;
import com.springboot.blog.utils.Params;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private PaginationUtil<Comment, CommentDto> paginationUtil;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, PaginationUtil<Comment, CommentDto> paginationUtil) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.paginationUtil = paginationUtil;
    }

    @Override
    public Pagination<CommentDto> getAllComments(Params params){
        Sort sort = params.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.toString()) ? Sort.by(params.getSortBy()).ascending()
                : Sort.by(params.getSortBy()).descending();
        Pageable pageable = PageRequest.of(params.getPageNo(), params.getPageSize(), sort);

        Page<Comment> page = commentRepository.findAll(pageable);
        // Check and parses response per parameters
        return validateAndReturnPaginationCommentDto(page, params);
    }

    @Override
    public Pagination<CommentDto> getPostComments(long postId, Params params){
        Sort sort = params.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.toString()) ? Sort.by(params.getSortBy()).ascending()
                : Sort.by(params.getSortBy()).descending();
        Pageable pageable = PageRequest.of(params.getPageNo(), params.getPageSize(), sort);
        postRepository.findById(postId).orElseThrow(()-> new ResourceNotFound("Post", "id", postId));

        Page<Comment> page = commentRepository.findCommentsByPostId(postId, pageable);
        // Check and parses response per parameters
        return validateAndReturnPaginationCommentDto(page, params);
    }


    @Override
    public CommentDto saveComment (CommentDto commentDto){
        Post checkPost = postRepository.findById(commentDto.getPostId()).orElseThrow(() -> new ResourceNotFound("Post", "id", commentDto.getPostId()));
        CommentDto response = null;
        if(checkPost != null){
            commentDto.setId(null);
            Comment comment = mapToEntity(commentDto);
            Comment savedComment = commentRepository.save(comment);
            response = mapToDto(savedComment);
        }
        return response;
    }

    @Override
    public void deleteComment(long id){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Comment", "id", id));
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateComment(long id, CommentDto commentDto){
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new ResourceNotFound("Comment", "id", id));
        if(!commentDto.getContent().trim().equalsIgnoreCase("") && commentDto.getContent() != null) {
            comment.setContent(commentDto.getContent().trim());
            commentRepository.save(comment);
        }
        CommentDto response = mapToDto(comment);
        return response;
    }




    // method to map Post object to PCommentostDto Object
    public CommentDto mapToDto(Comment comment){
        CommentDto results = new CommentDto();
        results.setId(comment.getId());
        results.setContent(comment.getContent());
        results.setPostId(comment.getPostId());
        results.setUserId(comment.getUserId());
        return results;
    }

    // method to map PostDto object to Post Object
    public Comment mapToEntity(CommentDto commentDto){
        Comment results = new Comment();
        results.setId(commentDto.getId());
        results.setContent(commentDto.getContent());
        results.setPostId(commentDto.getPostId());
        results.setUserId(commentDto.getUserId());
        return results;
    }


    // Validates Page Number and return pagination
    private Pagination<CommentDto> validateAndReturnPaginationCommentDto(Page<Comment> page, Params params) {
        if(page.getTotalPages() <= params.getPageNo())
            throw new ResourceNotFound("Comments", "Page Number", (long) params.getPageNo());

        List<CommentDto> results = new ArrayList<>();
        for(Comment comment : page.getContent()) {
            results.add(mapToDto(comment));
        }
        Pagination<CommentDto> response = paginationUtil.fetch(page, results);
        return response;
    }
}
