package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceIsEmpty;
import com.springboot.blog.exception.ResourceNotFound;
import com.springboot.blog.payload.NestedCommentDto;
import com.springboot.blog.payload.NestedPostDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.Pagination;
import com.springboot.blog.utils.PaginationUtil;
import com.springboot.blog.utils.Params;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private CommentService commentService;

    private PaginationUtil<Post, PostDto> paginationUtil;

    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository, CommentService commentService, PaginationUtil<Post, PostDto> paginationUtil, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.paginationUtil = paginationUtil;
        this.mapper = mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        // convert dto to entity
        Post post = mapToEntity(postDto);
        post.setComments(new ArrayList<>());
        Post savedPost = postRepository.save(post);
        // convert entity to dto
        PostDto responseDto = mapToDto(savedPost);
        return responseDto;
    }


    @Override
    public Pagination<PostDto> getAllPosts(Params params){
        Sort sort = params.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.toString()) ? Sort.by(params.getSortBy()).ascending()
                : Sort.by(params.getSortBy()).descending();
        Pageable pageable = PageRequest.of(params.getPageNo(), params.getPageSize(), sort);
        Page<Post> posts = postRepository.findAll(pageable);
        // checks page number and return pagination
        return validateAndReturnPaginationPostDto(posts, params);
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post", "id", id));
//        log.info("Post : {}", post.toString());
        // convert entity to dto
        PostDto results = mapToDto(post);
//        results.setComments(commentService.getPostComments(results.getId()));
        return results;
    }

    @Override
    public PostDto updatePost(Long id, PostDto postDto) {
         Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post", "id", id));
         if(postDto.getTitle() != null && postDto.getTitle().trim() != "") {
             int count = postRepository.countPostsByTitle(postDto.getTitle().trim());
             log.info("Title check --> {}", count);
             if(count == 0) {
                 post.setTitle(postDto.getTitle().trim());
             }
         }
         if(postDto.getContent() != null && postDto.getContent().trim() != "")
             post.setContent(postDto.getContent().trim());

         if(postDto.getDescription() != null && postDto.getDescription().trim() != "")
             post.setDescription(postDto.getDescription().trim());

         postRepository.save(post);
        return mapToDto(post);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post", "id", id));
        postRepository.delete(post);
        commentRepository.deleteCommentsByPostId(id);
    }

    @Override
    public NestedPostDto getPostByIdAsTweet(Long id) {
        PostDto post = getPostById(id);
        NestedPostDto response = mapper.map(post, NestedPostDto.class);
        if(response.getComments().size() > 0){
            response.getComments().forEach(nestedCommentDto -> {
                nestedCommentDto.setReplies(commentService.getCommentsReplies(nestedCommentDto.getId()));
            });
        }
        return response;
    }



    // method to map Post object to PostDto Object
    public PostDto mapToDto(Post post){
        PostDto results = mapper.map(post, PostDto.class);
        return results;
    }

    // method to map PostDto object to Post Object
    public Post mapToEntity(PostDto postDto){
        Post results = mapper.map(postDto, Post.class);
        return results;
    }



    // checks page number and return pagination
    private Pagination<PostDto> validateAndReturnPaginationPostDto(Page<Post> posts, Params params) {

        if((posts.getContent().size() < 1) && (posts.getNumber() == 0))
            throw new ResourceIsEmpty("Post", null);

        Boolean isLast = posts.getNumber() >= posts.getTotalPages();
        if(isLast)
            throw new ResourceNotFound("Posts", "Page", (long) params.getPageNo());

        List<PostDto> results = new ArrayList<>();
        for (Post post: posts.getContent()) {
            // convert entity to dto and append to list
            var postDto = mapToDto(post);
//            postDto.setComments(commentService.getPostComments(postDto.getId()));
            results.add(postDto);
        }
        Pagination<PostDto> response = paginationUtil.fetch(posts, results);
        return response;
    }



}
