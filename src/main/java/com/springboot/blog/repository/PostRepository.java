package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    Integer countPostsByTitle(String title);

    @Query("SELECT P FROM Post P WHERE P.title LIKE %:search% OR P.content LIKE %:search% OR P.description LIKE %:search%")
    Page<Post> findALlBySearch(Pageable pageable, String search);

}
