package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceIsEmpty;
import com.springboot.blog.exception.ResourceNotFound;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.CommentReplyDto;
import com.springboot.blog.payload.NestedCommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.utils.Pagination;
import com.springboot.blog.utils.PaginationUtil;
import com.springboot.blog.utils.Params;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private PaginationUtil<Comment, CommentDto> paginationUtil;

    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, PaginationUtil<Comment, CommentDto> paginationUtil, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.paginationUtil = paginationUtil;
        this.mapper = mapper;
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
    public List<CommentDto> getPostComments(long postId){
        postRepository.findById(postId).orElseThrow(()-> new ResourceNotFound("Post", "id", postId));

        List<Comment> comments = (List<Comment>) commentRepository.findCommentsByPostId(postId);
        List<CommentDto> results = new ArrayList<>();
        comments.forEach(comment -> {
            results.add(mapper.map(comment, CommentDto.class));
        });
        // Check and parses response per parameters
        return results;
    }

    @Override
    public List<NestedCommentDto> getCommentsReplies(long commentId) {
        commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFound("Comment", "id", commentId));

        List<Comment> comments = commentRepository.findCommentsByCommentId(commentId);
        List<NestedCommentDto> results = new ArrayList<>();
        comments.forEach(comment -> {
            results.add(mapper.map(comment, NestedCommentDto.class));
        });
        return results;
    }


    @Override
    public CommentDto saveComment (Long postId, CommentDto commentDto){
        Post checkPost = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "id", postId));
        CommentDto response = null;
        if(checkPost != null){
            commentDto.setId(null);
            Comment comment = mapToEntity(commentDto);
            comment.setPost(checkPost);
            Comment savedComment = commentRepository.save(comment);
            response = mapToDto(savedComment);
        }
        return response;
    }

    @Override
    public void deleteComment(long postId, long id){
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "id", postId));
        if(post != null){
            Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Comment", "id", id));
            commentRepository.delete(comment);
        }
    }

    @Override
    public CommentDto updateComment(long postId, CommentDto commentDto){
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "id", postId));
        if(post != null) {
            Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(() -> new ResourceNotFound("Comment", "id", commentDto.getId()));
            if (!commentDto.getBody().trim().equalsIgnoreCase("") && commentDto.getBody() != null) {
                comment.setBody(commentDto.getBody().trim());
                commentRepository.save(comment);
            }
            CommentDto response = mapToDto(comment);
            return response;
        }
        return null;
    }

    @Override
    public CommentReplyDto saveCommentReply(long postId, long commentId, CommentDto commentDto) {
        log.info("Inside saveCommentReply");
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFound("Comment", "Id", commentId));
        if(comment != null){
            Optional<Long> commentPostId = commentRepository.findPostIdByCommentId(commentId);
            postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "Id", postId));
            CommentReplyDto response = null;
            if(commentPostId.get() == postId || comment.getCommentId() != null){
                if(commentDto != null){
                    commentDto.setId(null);
                    Comment reply = mapToEntity(commentDto);
                    reply.setCommentId(commentId);
                    Comment savedComment = commentRepository.save(reply);
                    response = mapper.map(savedComment, CommentReplyDto.class);
                }
                return response;
            }else{
                throw new BlogAPIException("Given commentId does not belong to the specified postId", HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }

    @Override
    public Pagination<NestedCommentDto> addCommentReplies(Pagination<CommentDto> comments) {
        List<NestedCommentDto> data = new ArrayList<>();
        comments.getContent().forEach(commentDto -> {
            NestedCommentDto newComment = mapper.map(commentDto, NestedCommentDto.class);
            List<NestedCommentDto> replies = getCommentsReplies(newComment.getId());
            newComment.setReplies(getInnerReplies(replies));
            data.add(newComment);
        });
        Pagination<NestedCommentDto> response = new Pagination<>();
        response.setTotalElements(comments.getTotalElements());
        response.setTotalPages(comments.getTotalPages());
        response.setPageSize(comments.getPageSize());
        response.setPageNo(comments.getPageNo());
        response.setContent(data);
        response.setLast(comments.isLast());

        return response;
    }

    private List<NestedCommentDto> getInnerReplies(List<NestedCommentDto> replies) {
        for (NestedCommentDto nestedCommentDto : replies) {
            List<NestedCommentDto> innerReplies = getCommentsReplies(nestedCommentDto.getId());
            if(innerReplies.size() > 0)
                nestedCommentDto.setReplies(getInnerReplies(innerReplies));
        }
        return replies;
    }

    // method to map Comment object to CommentDto Object
    public CommentDto mapToDto(Comment comment){
        CommentDto results = mapper.map(comment, CommentDto.class);
        return results;
    }

    // method to map CommentDto object to Comment Object
    public Comment mapToEntity(CommentDto commentDto){
        Comment results = mapper.map(commentDto, Comment.class);
        return results;
    }


    // Validates Page Number and return pagination
    private Pagination<CommentDto> validateAndReturnPaginationCommentDto(Page<Comment> page, Params params) {

        if((page.getContent().size() < 1) && (page.getNumber() == 0))
            throw new ResourceIsEmpty("Comment", "Post");

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
