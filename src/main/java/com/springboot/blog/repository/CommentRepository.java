package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findCommentsByPostId(long postId, Pageable pageable);

    void deleteCommentsByPostId(long PostId);
}
