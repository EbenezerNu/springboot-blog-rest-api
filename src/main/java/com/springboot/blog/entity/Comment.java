package com.springboot.blog.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comments" )

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Long userId;
}
