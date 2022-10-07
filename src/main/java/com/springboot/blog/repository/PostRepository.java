package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    Integer countPostsByTitle(String title);
    
    Page<Post> findAllByCommentsContainingOrContentContainingOrDescriptionContaining (Pageable pageable, String search);

}
