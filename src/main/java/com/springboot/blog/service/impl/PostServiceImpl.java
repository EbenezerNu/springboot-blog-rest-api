package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFound;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.PaginationUtil;
import com.springboot.blog.utils.Pagination;
import com.springboot.blog.utils.Params;
import lombok.extern.slf4j.Slf4j;
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

    private PaginationUtil<Post, PostDto> paginationUtil;

    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository, PaginationUtil<Post, PostDto> paginationUtil) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.paginationUtil = paginationUtil;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        // convert dto to entity
        Post post = mapToEntity(postDto);
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
        return validateAndReturnPaginationCommentDto(posts, params);
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post", "id", id));
        // convert entity to dto
        PostDto results = mapToDto(post);
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



    // method to map Post object to PostDto Object
    public PostDto mapToDto(Post post){
        PostDto results = new PostDto();
        results.setId(post.getId());
        results.setTitle(post.getTitle());
        results.setDescription(post.getDescription());
        results.setContent(post.getContent());
        return results;
    }

    // method to map PostDto object to Post Object
    public Post mapToEntity(PostDto postDto){
        Post results = new Post();
        results.setId(postDto.getId());
        results.setTitle(postDto.getTitle());
        results.setDescription(postDto.getDescription());
        results.setContent(postDto.getContent());
        return results;
    }



    // checks page number and return pagination
    private Pagination<PostDto> validateAndReturnPaginationCommentDto(Page<Post> posts, Params params) {
        Boolean isLast = posts.getNumber() >= posts.getTotalPages();
        if(isLast)
            throw new ResourceNotFound("Posts", "Page", (long) params.getPageNo());

        List<PostDto> results = new ArrayList<>();
        for (Post post: posts.getContent()) {
            // convert entity to dto and append to list
            results.add(mapToDto(post));
        }
        Pagination<PostDto> response = paginationUtil.fetch(posts, results);
        return response;
    }



}
