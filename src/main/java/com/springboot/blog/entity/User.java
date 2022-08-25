package com.springboot.blog.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
public class User {
    @Id
    private Long id;

    @Column(name = "username")
    @NotNull
    @Size(min = 5, message = "Username should be at least 5 characters")
    private String username;

    @Column(name = "email")
    @Email
    private String email;

    @NotNull
    @Size(min = 8, message = "Password should be at least 8 characters")
    private String password;

}
