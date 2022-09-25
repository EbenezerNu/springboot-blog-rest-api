package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.CommentReplyDto;
import com.springboot.blog.payload.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findCommentsByPostId(long postId, Pageable pageable);

    Set<Comment> findCommentsByPostId(long postId);

    void deleteCommentsByPostId(long PostId);

    List<Comment> findCommentsByCommentId(long commentId);

    @Query(value = "SELECT c.post_id from comments c where c.id =:commentId", nativeQuery = true)
    long findPostIdByCommentId(long commentId);
}
