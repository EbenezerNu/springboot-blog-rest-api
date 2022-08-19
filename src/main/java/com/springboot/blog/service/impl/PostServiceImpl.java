package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFound;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
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
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDto> results = new ArrayList<>();
        for (Post post: posts) {
            // convert entity to dto and append to list
            results.add(mapToDto(post));
        }
        return results;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post", "id", id));
        // convert entity to dto
        PostDto results = mapToDto(post);
        return results;
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

}
