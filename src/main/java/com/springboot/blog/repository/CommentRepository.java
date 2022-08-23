package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.payload.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findCommentsByPostId(long postId, Pageable pageable);

    Set<Comment> findCommentsByPostId(long postId);

    void deleteCommentsByPostId(long PostId);
}
