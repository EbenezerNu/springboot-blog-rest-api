package com.springboot.blog.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
public class User {
    @Id
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    private String password;

}
